//package vn.tayjava.service.impl;
//
//import com.google.gson.Gson;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//import vn.tayjava.exception.ResourceNotFound;
//import vn.tayjava.model.Inventory;
//import vn.tayjava.repository.InventoryRepository;
//import vn.tayjava.service.InventoryService;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class InventoryServiceImpl implements InventoryService {
//
//    private final InventoryRepository inventoryRepository;
//
//    @Override
//    public long addInventory() {
//        return 0;
//    }
//
//    @Override
//    public void updateInventory(Inventory inventory) {
//        inventoryRepository.save(inventory);
//    }
//
//    private Inventory getInventory(long id) {
//        return inventoryRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Inventory not found"));
//    };
//
//    @KafkaListener(topics = "update-inventory-topic", groupId = "checkout-order-group")
//    public void updateInventory(String message)  {
//        log.info(message);
//
//        OrderMessage orderMessage = new Gson().fromJson(message, OrderMessage.class);
//
//        // TODO goi GRPC qua order service để lấy ra toàn bộ product
//
//        // Cap nhat so luong hàng ton kho()
//        updateInventory(Inventory.builder().build());
//    }
//
//    @Getter
//    @Builder
//    @AllArgsConstructor
//    private static class OrderMessage {
//        private String orderId;
//        private String status;
//    }
//}
