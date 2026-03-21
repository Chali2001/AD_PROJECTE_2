package com.ra34.projecte2.repository;

import com.ra34.projecte2.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    
    // Buscar por nombre que contenga el fragmento y status true
    List<Product> findByNameContainingAndStatusTrue(String nameFragment);

    // Búsqueda para ordenar por precio (usando la clase Sort dinámica)
    List<Product> findByStatusTrue(Sort sort);

    
    // Buscar por rango de precios y nombre
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :priceMin AND :priceMax AND p.name LIKE %:nameFragment% AND p.status = true")
    List<Product> findByPriceRangeAndNameAndStatusTrue(
            @Param("priceMin") Double priceMin, 
            @Param("priceMax") Double priceMax, 
            @Param("nameFragment") String nameFragment,
            Pageable pageable); 

    //  calidad-precio (Rating / Price)
    @Query("SELECT p FROM Product p WHERE p.status = true AND p.rating IS NOT NULL AND p.price > 0 ORDER BY (p.rating / p.price) DESC")
    List<Product> findTopBestQualityPriceRatio(Pageable pageable);
}