package vn.tayjava.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tayjava.controller.request.InventoryRequest;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @GetMapping("/list")
    public String getAll() {
        return "inventory list";
    }

    @PostMapping
    public ResponseEntity reserveInventory(@RequestBody InventoryRequest inventoryRequest) {
        System.out.println("Inventory Reserved");

        // Logic để trừ hàng trong kho
        return ResponseEntity.ok("Inventory Reserved");
    }

    @PostMapping("/compensate")
    public ResponseEntity releaseInventory(@RequestBody InventoryRequest inventoryRequest) {
        System.out.println("Inventory Released");
        // Logic hoàn trả hàng trong trường hợp cần bù trừ
        return ResponseEntity.ok("Inventory Released");
    }
}
