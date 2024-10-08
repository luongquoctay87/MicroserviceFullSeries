package vn.tayjava.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@ToString
@Document(indexName = "products")
public class ProductDocument {
    @Id
    private String id;

    private String name;
    private String description;
    private double price;
}

