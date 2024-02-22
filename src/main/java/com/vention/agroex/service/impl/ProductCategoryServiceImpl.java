package com.vention.agroex.service.impl;

import com.vention.agroex.entity.ProductCategoryEntity;
import com.vention.agroex.filter.ProductCategorySpecificationsBuilder;
import com.vention.agroex.repository.ProductCategoryRepository;
import com.vention.agroex.service.ProductCategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategoryEntity save(ProductCategoryEntity productCategoryEntity) {
        if (productCategoryEntity.getParentId() == null) {
            productCategoryEntity.setParentId(0L);
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
    public List<ProductCategoryEntity> getSubcategoriesById(Long parentId) {
        return productCategoryRepository.findProductCategoryListByParentId(parentId);
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
        productCategory.setParentId(fetchedCategory.getParentId());
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public List<ProductCategoryEntity> getWithFilters(String filters) {
        var builder = new ProductCategorySpecificationsBuilder();

        Pattern pattern = Pattern.compile("(\\w+?)([:<>])(\\w+?),");
        var matcher = pattern.matcher(filters + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<ProductCategoryEntity> spec = builder.build();
        return productCategoryRepository.findAll(spec);
    }
}
