package vn.tayjava.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.tayjava.controller.request.PlaceOrderRequest;
import vn.tayjava.model.Order;
import vn.tayjava.model.OrderItem;
import vn.tayjava.repository.OrderRepository;
import vn.tayjava.service.OrderService;

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
        order.setUserId(request.getUserId());
        order.setTotalPrice(request.getTotalPrice());
        order.setPaymentId(request.getPaymentId());
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        List<OrderItem> orderItems = request.getOrderItems().stream().map(
                item -> OrderItem.builder()
                        .orderId(order.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .unit(item.getUnit())
                        .price(item.getPrice())
                        .build()
        ).toList();
        order.setOrderItems(orderItems);

        orderRepository.save(order);

        return order.getId();
    }
}
