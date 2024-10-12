package vn.tayjava.controller.request;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class ProductUpdateRequest implements Serializable {
    private long id;
    private String name;
    private String description;
    private double price;
    private int userId;
}

