package com.ra34.projecte2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.model.ProductCondition;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Cerca per condició, només productes actius
	List<Product> findByConditionAndStatusTrue(ProductCondition condition);
    // Ordena per rating (asc/desc), només productes actius
	List<Product> findByStatusTrueOrderByRatingAsc();
    // Ordena per rating (asc/desc), només productes actius
	List<Product> findByStatusTrueOrderByRatingDesc();

}
