package vn.tayjava.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.tayjava.controller.request.PlaceOrderRequest;
import vn.tayjava.service.OrderService;

@Validated
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j(topic = "ORDER-CONTROLLER")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/list")
    public String getAll() {
        return "order list";
    }

    @PostMapping("/add")
    public ResponseEntity<String> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        log.info("placeOrder request: {}", request);
        return ResponseEntity.ok(orderService.addOrder(request));
    }
}
