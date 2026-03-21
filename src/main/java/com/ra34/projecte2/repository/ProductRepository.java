package com.ra34.projecte2.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.model.ProductCondition;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Buscar por nombre que contenga el prefijo y status true
    List<Product> findByNameContainingAndStatusTrue(String prefix);

    // Búsqueda para ordenar por precio (usando la clase Sort dinámica)
    List<Product> findByStatusTrue(Sort sort);

    // Buscar por rango de precios y nombre
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :priceMin AND :priceMax AND p.name LIKE %:prefix% AND p.status = true")
    List<Product> findByPriceRangeAndNameAndStatusTrue(
            @Param("priceMin") Double priceMin,
            @Param("priceMax") Double priceMax,
            @Param("prefix") String prefix,
            Pageable pageable);

    // calidad-precio (Rating / Price)
    @Query("SELECT p FROM Product p WHERE p.status = true AND p.rating IS NOT NULL AND p.price > 0 ORDER BY (p.rating / p.price) DESC")
    List<Product> findTopBestQualityPriceRatio(Pageable pageable);

    // Cerca per condició, només productes actius
    List<Product> findByConditionAndStatusTrue(ProductCondition condition);

    // Ordena per rating (asc/desc), només productes actius
    List<Product> findByStatusTrueOrderByRatingAsc();

    // Ordena per rating (asc/desc), només productes actius
    List<Product> findByStatusTrueOrderByRatingDesc();

    // Filtra per rang de rating i només productes actius
    List<Product> findByRatingBetweenAndStatusTrue(Double ratingMin, Double ratingMax);

    // Top 10 productes NOU, actius, ordenats per millor rating
    List<Product> findTop10ByConditionAndStatusTrueOrderByRatingDesc(ProductCondition condition);

    // Paginació de productes actius, 5 per pàgina
    Page<Product> findByStatusTrue(Pageable pageable);
}
