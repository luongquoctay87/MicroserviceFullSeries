package vn.tayjava.service;

import vn.tayjava.controller.request.ProductCreationRequest;
import vn.tayjava.controller.request.ProductUpdateRequest;
import vn.tayjava.model.ProductDocument;

import java.util.List;

public interface ProductService {

    long addProduct(ProductCreationRequest request);

    List<ProductDocument> searchProducts(String name);

    void updateUser(ProductUpdateRequest product);

    void deleteProduct(long productId);
}
