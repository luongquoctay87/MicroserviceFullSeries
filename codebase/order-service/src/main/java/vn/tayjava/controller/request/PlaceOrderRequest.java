package vn.tayjava.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@ToString
public class PlaceOrderRequest implements Serializable {

    @NotNull(message = "userId must be not null")
    private Long userId;

    @NotNull(message = "totalPrice must be not null")
    private Double totalPrice;

    @NotNull(message = "paymentId must be not null")
    private Long paymentId;

    @NotEmpty(message = "orderItems must be not empty")
    private List<OrderItemRequest> orderItems;

}
