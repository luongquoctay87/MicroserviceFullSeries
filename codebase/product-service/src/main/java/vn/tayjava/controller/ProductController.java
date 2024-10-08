package vn.tayjava.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.tayjava.controller.request.ProductCreationRequest;
import vn.tayjava.model.ProductDocument;
import vn.tayjava.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
@Slf4j(topic = "PRODUCT-CONTROLLER")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public List<ProductDocument> getProductList(@RequestParam(required = false) String name) {
        log.info("-----[ getProductList ]-----");
        return productService.searchProducts(name);
    }

    @PostMapping("/add")
    public long addProduct(@RequestBody ProductCreationRequest request) {
        log.info("request: {}", request);
        return productService.addProduct(request);
    }
}
