package vn.tayjava.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryRequest {
    private String orderId;
    private Integer quantity;
}
