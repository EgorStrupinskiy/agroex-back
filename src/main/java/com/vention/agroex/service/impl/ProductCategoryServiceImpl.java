package com.vention.agroex.service.impl;

import com.vention.agroex.entity.ProductCategoryEntity;
import com.vention.agroex.repository.ProductCategoryRepository;
import com.vention.agroex.service.ProductCategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategoryEntity save(ProductCategoryEntity productCategoryEntity) {
        if (productCategoryEntity.getParent() == null) {
            productCategoryEntity.setParent(
                    productCategoryRepository.getRootCategory()
            );
        }
        productCategoryEntity.setTitle(StringUtils.normalizeSpace(productCategoryEntity.getTitle()));
        return productCategoryRepository.save(productCategoryEntity);
    }

    @Override
    public ProductCategoryEntity getById(Long id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("There is no product category with id %d", id)));
    }

    @Override
    public List<ProductCategoryEntity> getSubcategoriesById(Long parentId, Boolean lotExisted) {
        var categories = lotExisted ? productCategoryRepository.findProductCategoryListByParentIdAndLotsIsNotEmpty(parentId) :
                productCategoryRepository.findProductCategoryListByParentId(parentId);
        return categories.orElseThrow(
                () -> new EntityNotFoundException(String.format("There is no product category with parentId %d", parentId)));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productCategoryRepository.deleteById(id);
        productCategoryRepository.deleteByParentId(id);
    }

    @Override
    public List<ProductCategoryEntity> getAll() {
        return productCategoryRepository.findAll();
    }

    @Override
    public ProductCategoryEntity update(Long id, ProductCategoryEntity productCategory) {
        var fetchedCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("There is no category with id %d", id)));
        productCategory.setId(id);
        productCategory.setParent(fetchedCategory.getParent());
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public List<ProductCategoryEntity> getAllSubCategories(Long productCategoryId) {
        var categories = getAll();
        return categories.stream()
                .filter(category -> searchParent(category, productCategoryId))
                .toList();
    }

    private boolean searchParent(ProductCategoryEntity entity, Long searchId){
        if (entity.getId().equals(searchId))
            return true;
        if (entity.getParent() == null)
            return false;
        if (entity.getParent().getId().equals(searchId))
            return true;
        return searchParent(entity.getParent(), searchId);
    }

}
