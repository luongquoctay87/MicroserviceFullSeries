package vn.tayjava.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "order")
public class Order {

    @Id
    private String id;

    private Long customerId;

    private Long amount;

    private String currency;

    private String paymentMethod;

    private int status;

    private String statusName;

    private Date createdAt;

    private Date updatedAt;

    private List<OrderItem> orderItems;
}
