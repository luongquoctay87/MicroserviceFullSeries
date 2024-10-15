package vn.tayjava.controller;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class PaymentInfoRequest implements Serializable {
    private int amount;
    private String currency;
    private String receiptEmail;
}