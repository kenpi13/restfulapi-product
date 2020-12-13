package com.example.restfulapiproduct.controller;

import com.example.restfulapiproduct.entity.ProductEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    public static final int SAMPLE_PRICE = 3000;

    @GetMapping
    public ResponseEntity<ProductEntity> sample() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setTitle("shoes");
        productEntity.setDescription("this is best shoes");
        productEntity.setPrice(SAMPLE_PRICE);
        return new ResponseEntity<>(productEntity, HttpStatus.OK);
    }
}
