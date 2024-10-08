package vn.tayjava.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.tayjava.controller.request.ProductCreationRequest;
import vn.tayjava.model.Product;
import vn.tayjava.model.ProductDocument;
import vn.tayjava.repository.ProductRepository;
import vn.tayjava.repository.ProductSearchRepository;
import vn.tayjava.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT-SERVICE")
public class ProductServiceImpl implements ProductService {

    private final ProductSearchRepository productSearchRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addProduct(ProductCreationRequest request) {
        log.info("Add product {}", request);

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        productRepository.save(product);

        if (product.getId() != null) {
            ProductDocument productDocument = new ProductDocument();
            productDocument.setName(request.getName());
            productDocument.setDescription(request.getDescription());
            productDocument.setPrice(request.getPrice());

            productSearchRepository.save(productDocument);
            log.info("Save productDocument", productDocument);
        }

        return product.getId();

    }

    @Override
    public List<ProductDocument> searchProducts(String name) {
        log.info("Search products by name {}", name);

        List<ProductDocument> productDocuments = new ArrayList<>();

        if (StringUtils.hasLength(name)) {
            productDocuments = productSearchRepository.findByNameContaining(name);
        } else {
            Iterable<ProductDocument> documents = productSearchRepository.findAll();
            for (ProductDocument productDocument : documents) {
                productDocuments.add(productDocument);
            }
        }

        return productDocuments;
    }
}
