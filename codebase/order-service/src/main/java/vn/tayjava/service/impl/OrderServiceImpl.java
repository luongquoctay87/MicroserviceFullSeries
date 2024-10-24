package vn.tayjava.service.impl;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.tayjava.common.OrderStatus;
import vn.tayjava.controller.request.PlaceOrderRequest;
import vn.tayjava.controller.request.PmtOrderMessage;
import vn.tayjava.model.Order;
import vn.tayjava.model.OrderItem;
import vn.tayjava.repository.OrderRepository;
import vn.tayjava.service.OrderService;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ORDER-SERVICE")
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.checkoutOrder}")
    private String checkoutOrderTopic;

    @Value("${spring.kafka.topic.updateInventory}")
    private String updateInventory;

    @Value("${spring.kafka.topic.notifyOrderStatus}")
    private String notifyOrderStatus;

    @Override
    public String addOrder(PlaceOrderRequest request) {
        log.info("Add order request: {}", request);

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setAmount(request.getAmount());
        order.setCurrency(request.getCurrency());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus(OrderStatus.NEW.getValue());
        order.setStatusName(OrderStatus.NEW.name());
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        List<OrderItem> orderItems = request.getOrderItems().stream().map(
                item -> OrderItem.builder()
                        .orderId(order.getId())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .unit(item.getUnit())
                        .price(item.getPrice())
                        .build()
        ).toList();
        order.setOrderItems(orderItems);

        orderRepository.save(order);

        return order.getId();
    }

    @Override
    public BufferedImage generateQRCodeImage(String qrcode) throws WriterException {
        log.info("Generate QR code image: {}", qrcode);

        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(qrcode, BarcodeFormat.QR_CODE, 200, 200);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * Generate barcode EAN13 {Mã quốc gia, mã doanh nghiệp, mã sản phẩm và cuối cùng là số kiểm tra}
     *
     * Mã quốc gia: Sử dụng 2 (hoặc 3) ký tự đầu tiên làm mã quốc gia.
     * Mã doanh nghiệp: Sẽ có 5 số nếu chỉ dùng 2 số cho mã quốc gia hoặc có 4 số nếu mã quốc gia dùng đến 3 số.
     * Mã sản phẩm: Với 5 số tiếp theo sẽ là mã sản phẩm của nhà sản xuất.
     * Số kiểm tra: Số cuối cùng là số kiểm tra, phụ thuộc vào 12 số trước nó.
     *
     * @param barcode
     * @return
     * @throws WriterException
     *
     * Format Barcode: https://help.accusoft.com/BarcodeXpress/v13.2/BxNodeJs/ean_13.html
     */
    @Override
    public BufferedImage generateBarCodeImage(String barcode) throws WriterException {
        log.info("Generate Bar code image: {}", barcode);

        // TODO validate EAN13

        EAN13Writer barcodeWriter = new EAN13Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcode, BarcodeFormat.EAN_13, 300, 150);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    @Override
    public String checkoutOrder(String orderId) {
        log.info("Checkout order: {}", orderId);

        Order order = orderRepository.findById(orderId).get();

        PmtOrderMessage message = new PmtOrderMessage();
        message.setOrderId(order.getId());
        message.setCustomerId(order.getCustomerId());
        message.setAmount(order.getAmount());
        message.setCurrency(order.getCurrency());
        message.setPaymentMethod(order.getPaymentMethod());

        Gson gson = new Gson();
        String json = gson.toJson(message);

        kafkaTemplate.send(checkoutOrderTopic, json);

        return "Processing";
    }

    @KafkaListener(topics = "checkout-order-call-back-topic", groupId = "checkout-order-call-back-group")
    public void callBackOrder(String message)  {
        log.info("callBackOrder = {}", message);

        Gson gson = new Gson();
        CallBackMessage callBackMessage = gson.fromJson(message, CallBackMessage.class);

        Order order = orderRepository.findById(callBackMessage.orderId).get();
        if (callBackMessage.paymentStatus.equals("PAID") ) {
            // TODO validate enum order status
            order.setStatusName(OrderStatus.PAID.name());
            order.setStatus(OrderStatus.PAID.getValue());
           //  push inventory
            kafkaTemplate.send(updateInventory, gson.toJson(OrderMessage.builder().orderId(order.getId()).status(order.getStatusName()).build()));
        } else {
            order.setStatusName(OrderStatus.CANCELED.name());
            order.setStatus(OrderStatus.CANCELED.getValue());
        }

        orderRepository.save(order);

        // push message toi notification
        kafkaTemplate.send(notifyOrderStatus, gson.toJson(OrderMessage.builder().orderId(order.getId()).status(order.getStatusName()).build()));

    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class CallBackMessage {
        private String orderId;
        private String paymentStatus;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class OrderMessage {
        private String orderId;
        private String status;
    }

}
