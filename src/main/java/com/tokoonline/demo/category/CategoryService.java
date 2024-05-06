package com.tokoonline.demo.category;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tokoonline.demo.category.model.Category;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;

    public List<Category> fetchAllById(List<UUID> id){
        List<Category> foundCategory = categoryRepository.findAllById(id);
        if(foundCategory.size() == 0){
            throw new CategoryNotFoundException();
        }

        return foundCategory;
    }
}
