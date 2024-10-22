package vn.tayjava.controller.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PmtOrderMessage {
    private String orderId;
    private Long customerId;
    private Long amount;
    private String currency;
    private String paymentMethod;

}
