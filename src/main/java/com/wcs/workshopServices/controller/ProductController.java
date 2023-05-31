package com.wcs.workshopServices.controller;

import com.wcs.workshopServices.entity.Product;
import com.wcs.workshopServices.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<?> getProducts(@RequestParam (required = false) Long category) {
        List<Product> products;
        if(category != null) {
                products = productRepository.findProductsByCategory_Id(category);
        } else {
            products = productRepository.findAll();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            return ResponseEntity.ok(product);
        } else {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ce produit n'existe pas");
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product body) {
        Product productToCreate = new Product();
        productToCreate.setName(body.getName());
        productToCreate.setCategory(body.getCategory());

        String uuid = UUID.randomUUID().toString();
        String formattedProductName = productToCreate.getName().replace('-', ' ');
        String slug = formattedProductName.replaceAll("[^a-zA-Z0-9]", "-")  + '-' + uuid;
        productToCreate.setSlug(slug);

        Product createdProduct = productRepository.save(productToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProductById(@PathVariable Long productId, @RequestBody Product body) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            Product productToUpdate = optionalProduct.get();
            productToUpdate.setName(body.getName());
            productToUpdate.setCategory(body.getCategory());

            String uuid = UUID.randomUUID().toString();
            String formattedProductName = productToUpdate.getName().replace('-', ' ');
            String slug = formattedProductName.replaceAll("[^a-zA-Z0-9]", "-")  + '-' + uuid;
            productToUpdate.setSlug(slug);

            Product updatedProduct = productRepository.save(productToUpdate);
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ce produit n'existe pas");
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            productRepository.deleteById(productId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ce produit n'existe pas");
        }
    }
}

