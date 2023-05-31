package com.wcs.workshopServices.repository;

import com.wcs.workshopServices.entity.Category;
import com.wcs.workshopServices.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByCategory_Id(Long categoryId);
}
