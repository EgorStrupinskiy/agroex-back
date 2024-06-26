package com.vention.agroex.util.validator;

import com.vention.agroex.entity.ProductCategoryEntity;
import com.vention.agroex.exception.InvalidArgumentException;
import com.vention.agroex.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductCategoryEntityValidator implements Validator {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductCategoryEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductCategoryEntity productCategoryEntity = (ProductCategoryEntity) target;
        Map<String, Object> errorsMap = new HashMap<>();
        if (productCategoryRepository.findByTitleAndIdNot(productCategoryEntity.getId(), productCategoryEntity.getTitle()).isPresent())
            errorsMap.put("title", "Product category with this title already exists");
        if (productCategoryRepository.findById(productCategoryEntity.getParent().getId()).isEmpty())
            errorsMap.put("parentId", String.format("There is no product category with id %d", productCategoryEntity.getParent().getId()));
        errors.getFieldErrors()
                .forEach(error -> errorsMap.put(error.getField(), error.getDefaultMessage()));
        if (errorsMap.size() > 0)
            throw new InvalidArgumentException(errorsMap, "Invalid arguments!");
    }
}
