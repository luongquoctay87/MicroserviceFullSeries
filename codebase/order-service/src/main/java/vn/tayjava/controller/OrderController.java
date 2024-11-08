package vn.tayjava.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.tayjava.controller.request.OrderRequest;
import vn.tayjava.controller.request.PlaceOrderRequest;
import vn.tayjava.service.OrderService;

import java.awt.image.BufferedImage;

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

    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        log.info("placeOrder request: {}", request);
        return ResponseEntity.ok(orderService.addOrder(request));
    }

    @PostMapping(path = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateQRCodeImage(@RequestParam String qrCode) throws Exception {
        return ResponseEntity.ok(orderService.generateQRCodeImage(qrCode));
    }

    @PostMapping(path = "/bar-code", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateBarcode(@RequestParam String barcode) throws Exception {
        log.info("generateBarcode request: {}", barcode);
        return ResponseEntity.ok(orderService.generateBarCodeImage(barcode));
    }

    @PostMapping("/checkout/{orderId}")
    public ResponseEntity<String> checkoutOrder(@PathVariable String orderId) {
        log.info("checkoutOrder request: {}", orderId);
        return ResponseEntity.ok(orderService.checkoutOrder(orderId));
    }

    @PostMapping("")
    public ResponseEntity createOrder(@RequestBody OrderRequest orderRequest) {
        // Logic để tạo đơn hàng
        System.out.println("Order Created");
        return ResponseEntity.ok("Order Created");
    }

    @PostMapping("/compensate")
    public ResponseEntity cancelOrder(@RequestBody OrderRequest orderRequest) {
        // Logic để hủy đơn hàng trong trường hợp cần bù trừ
        System.out.println("Order Canceled");
        return ResponseEntity.ok("Order Canceled");
    }


}
