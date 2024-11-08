package vn.tayjava.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderRequest implements Serializable {
    private String orderId;
    private Double amount;
}
