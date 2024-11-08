package vn.tayjava.controller;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaymentRequest implements Serializable {
    private String orderId;
    private Double amount;
}
