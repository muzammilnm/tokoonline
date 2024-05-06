package com.tokoonline.demo.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tokoonline.demo.category.CategoryService;
import com.tokoonline.demo.category.model.Category;
import com.tokoonline.demo.product.model.Product;
import com.tokoonline.demo.product.model.dto.SearchProductRequestDto;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    
    private final ProductRepostitory productRepostitory;
    private final CategoryService categoryService;

    public List<Product> fetchAll(){
        return productRepostitory.findAll();
    }

    public Page<Product> search(SearchProductRequestDto request){
        Specification<Product> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(Objects.nonNull(request.getName())){
                predicates.add(builder.like(root.get("name"), "%" + request.getName() + "%"));
            }
            if(Objects.nonNull(request.getPrice())){
                predicates.add(builder.equal(root.get("price"), request.getPrice()));
            }
            if(request.getCategoryIds().size() != 0){
                predicates.add(root.get("categories").get("id").in(request.getCategoryIds()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Product> products = productRepostitory.findAll(specification, pageable);

        return products;
    }

    public Product fetchById(UUID id){
        return productRepostitory.findById(id).orElseThrow(() -> new ProductNotFoundException());
    }

    public Product add(Product product, List<UUID> categoryId){
        List<Category> foundCategory = categoryService.fetchAllById(categoryId);
        Product newProduct = Product.builder() 
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .categories(foundCategory)
            .build();

        return productRepostitory.save(newProduct);
    }

    public Product update(Product product){
        Product foundProduct = productRepostitory.findById(product.getId()).orElseThrow(() -> new ProductNotFoundException());

        foundProduct.setName(product.getName());
        foundProduct.setDescription(product.getDescription());
        foundProduct.setPrice(product.getPrice());
        foundProduct.setStock(product.getStock());
        return productRepostitory.save(foundProduct);
    }

    public Boolean deleteById(UUID id){
        productRepostitory.findById(id).orElseThrow(() -> new ProductNotFoundException());
        productRepostitory.deleteById(id);
        return true;
    }
}
