package vn.tayjava.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document(collection = "order_item")
public class OrderItem {

    @Id
    private String id;

    private String orderId;

    private Long productId;

    private String productName;

    private Integer quantity;

    private Double price;

    private String unit;
}
