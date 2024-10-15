package vn.tayjava.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tayjava.common.OrderStatus;
import vn.tayjava.controller.request.PlaceOrderRequest;
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

    @Override
    public String addOrder(PlaceOrderRequest request) {
        log.info("Add order request: {}", request);

        Order order = new Order();
        order.setUserId(request.getCustomerId());
        order.setTotalPrice(request.getTotalPrice());
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

        // TODO push message to Kafka
        //Order order = orderRepository.findById(orderId).get();

        return "Processing";
    }
}
