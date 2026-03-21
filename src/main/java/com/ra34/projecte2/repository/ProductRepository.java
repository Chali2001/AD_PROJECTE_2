package com.ra34.projecte2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ra34.projecte2.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
