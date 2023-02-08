package com.chrissy.springbootmall.dao;

import com.chrissy.springbootmall.dto.ProductQueryParams;
import com.chrissy.springbootmall.dto.ProductRequest;
import com.chrissy.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {
    List<Product> getProducts(ProductQueryParams productQueryParams);

    Integer countProduct(ProductQueryParams productQueryParams);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,ProductRequest productRequest);

    void deleteProduct(Integer productId);
}
