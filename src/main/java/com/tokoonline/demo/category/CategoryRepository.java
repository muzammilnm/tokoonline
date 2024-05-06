package com.tokoonline.demo.category;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tokoonline.demo.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
