package com.product.service;

import com.product.entity.Product;
import com.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "products")
public class ProductService {

    private final ProductRepository productRepository;

    @Cacheable(key = "#id")
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

}
