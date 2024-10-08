package vn.tayjava.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@ToString
public class ProductCreationRequest {
    private String name;
    private String description;
    private double price;
}

