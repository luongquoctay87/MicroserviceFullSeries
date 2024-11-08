package vn.tayjava.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.tayjava.controller.request.OrderRequest;

@Service
@RequiredArgsConstructor
public class OrderSagaOrchestrator {

    private final RestTemplate restTemplate;

    public void startOrderSaga(OrderRequest orderRequest) {
        try {
            // Bước 1: Tạo đơn hàng
            restTemplate.postForEntity("http://localhost:8084/order", orderRequest, String.class);

            // Bước 2: Xử lý thanh toán
            restTemplate.postForEntity("http://localhost:8085/payment", orderRequest, String.class);

            // Bước 3: Trừ hàng tồn kho
            restTemplate.postForEntity("http://localhost:8086/inventory", orderRequest, String.class);

            // Saga thành công
            System.out.println("Order Saga completed successfully.");

        } catch (Exception e) {
            System.out.println("Saga failed. Compensating transactions...");
            compensateOrderSaga(orderRequest);
        }
    }

    private void compensateOrderSaga(OrderRequest orderRequest) {
        // Gọi đến các endpoint để bù trừ các bước đã thành công
        restTemplate.postForEntity("http://localhost:8084/order/compensate", orderRequest, String.class);
        restTemplate.postForEntity("http://localhost:8085/payment/compensate", orderRequest, String.class);
        restTemplate.postForEntity("http://localhost:8086/inventory/compensate", orderRequest, String.class);
    }
}
