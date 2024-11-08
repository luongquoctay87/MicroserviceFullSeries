package vn.tayjava.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tayjava.controller.request.OrderRequest;
import vn.tayjava.service.OrderSagaOrchestrator;

@RestController
@RequestMapping("/order-saga")
@RequiredArgsConstructor
public class OrderSagaController {

    private final OrderSagaOrchestrator orchestrator;

    @PostMapping
    public ResponseEntity startOrderSaga(@RequestBody OrderRequest orderRequest) {
        orchestrator.startOrderSaga(orderRequest);
        return ResponseEntity.ok("Order Saga started");
    }
}
