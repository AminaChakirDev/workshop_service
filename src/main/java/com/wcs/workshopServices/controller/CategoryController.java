package com.wcs.workshopServices.controller;


import com.wcs.workshopServices.entity.Category;
import com.wcs.workshopServices.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<?> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if(optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cette catégorie n'existe pas");
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category body) {
        Category categoryToCreate = new Category();
        categoryToCreate.setTitle(body.getTitle());

        String formattedCategoryName = categoryToCreate.getTitle().replace('-', ' ');
        String slug = formattedCategoryName.replaceAll("[^a-zA-Z0-9]", "-");
        categoryToCreate.setSlug(slug);

        Category createdCategory = categoryRepository.save(categoryToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId, @RequestBody Category body) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if(optionalCategory.isPresent()) {
            Category categoryToUpdate = optionalCategory.get();
            categoryToUpdate.setTitle(body.getTitle());

            String formattedCategoryName = categoryToUpdate.getTitle().replace('-', ' ');
            String slug = formattedCategoryName.replaceAll("[^a-zA-Z0-9]", "-");
            categoryToUpdate.setSlug(slug);

            Category updatedCategory = categoryRepository.save(categoryToUpdate);
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cette catégorie n'existe pas");
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteById(@PathVariable Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if(optionalCategory.isPresent()) {
            categoryRepository.deleteById(categoryId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cette catégorie n'existe pas");
        }
    }
}

