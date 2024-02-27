package com.vention.agroex.service;

import com.vention.agroex.entity.ProductCategoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductCategoryService {
    ProductCategoryEntity save(ProductCategoryEntity productCategoryEntity);

    ProductCategoryEntity getById(Long id);

    List<ProductCategoryEntity> getSubcategoriesById(Long id, Boolean lotExisted);

    void deleteById(Long id);

    List<ProductCategoryEntity> getAll();

    ProductCategoryEntity update(Long id, ProductCategoryEntity productCategoryEntity);

    List<ProductCategoryEntity> getWithFilters(String filters);
}
