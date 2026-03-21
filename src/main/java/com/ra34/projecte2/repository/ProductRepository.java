package com.ra34.projecte2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.model.ProductCondition;

/**
 * Repositorio de productos que extiende JpaRepository.
 * Proporciona métodos de Query Derivation y JPQL para consultas específicas.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ==================== CRUD BÁSICO Y BÚSQUEDAS SIMPLES ====================

    /**
     * Obtiene todos los productos activos con paginación.
     *
     * @param pageable parámetros de paginación
     * @return página de productos activos
     */
    Page<Product> findByStatusTrue(Pageable pageable);

    /**
     * Obtiene todos los productos activos ordenados dinámicamente.
     *
     * @param sort parámetros de ordenación
     * @return lista de productos activos ordenados
     */
    List<Product> findByStatusTrue(Sort sort);

    // ==================== QUERY DERIVATION ====================

    /**
     * Busca productos por nombre (búsqueda parcial) que estén activos.
     * Endpoint: GET /api/products/search/name?prefix=...
     *
     * @param prefix prefijo del nombre a buscar
     * @return lista de productos cuyo nombre contiene el prefijo y status=true
     */
    List<Product> findByNameContainingAndStatusTrue(String prefix);

    /**
     * Busca productos por condición que estén activos.
     * Endpoint: GET /api/products/search/condition?condition=...
     *
     * @param condition condición del producto
     * @return lista de productos con esa condición y status=true
     */
    List<Product> findByConditionAndStatusTrue(ProductCondition condition);

    /**
     * Obtiene productos activos ordenados por rating ascendente.
     *
     * @return lista de productos ordenados por rating ASC
     */
    List<Product> findByStatusTrueOrderByRatingAsc();

    /**
     * Obtiene productos activos ordenados por rating descendente.
     *
     * @return lista de productos ordenados por rating DESC
     */
    List<Product> findByStatusTrueOrderByRatingDesc();

    /**
     * Obtiene productos activos ordenados por precio ascendente.
     *
     * @return lista de productos ordenados por precio ASC
     */
    List<Product> findByStatusTrueOrderByPriceAsc();

    /**
     * Obtiene productos activos ordenados por precio descendente.
     *
     * @return lista de productos ordenados por precio DESC
     */
    List<Product> findByStatusTrueOrderByPriceDesc();

    /**
     * Busca productos cuyo rating está en un rango y estén activos.
     *
     * @param ratingMin rating mínimo
     * @param ratingMax rating máximo
     * @return lista de productos en el rango de rating con status=true
     */
    List<Product> findByRatingBetweenAndStatusTrue(Double ratingMin, Double ratingMax);

    /**
     * Obtiene los top 10 productos nuevos ordenados por mejor rating.
     *
     * @param condition condición del producto
     * @return lista de hasta 10 productos nuevos con mejor rating
     */
    List<Product> findTop10ByConditionAndStatusTrueOrderByRatingDesc(ProductCondition condition);

    // ==================== CONSULTAS JPQL ====================

    /**
     * Busca productos por rango de precios y nombre (búsqueda parcial).
     * Endpoint: GET /api/products/search/price-range?priceMin=...&priceMax=...&prefix=...
     *
     * @param priceMin precio mínimo (incluido)
     * @param priceMax precio máximo (incluido)
     * @param prefix prefijo del nombre
     * @param pageable parámetros de paginación
     * @return página de productos en rango de precios y nombre contiene prefix con status=true
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :priceMin AND :priceMax AND p.name LIKE %:prefix% AND p.status = true")
    List<Product> findByPriceRangeAndNameAndStatusTrue(
            @Param("priceMin") Double priceMin,
            @Param("priceMax") Double priceMax,
            @Param("prefix") String prefix,
            Pageable pageable);

    /**
     * Obtiene los 5 productos con mejor relación calidad-precio (rating/precio).
     * Endpoint: GET /api/products/search/top5-quality-price
     *
     * @param pageable parámetros de paginación (limit)
     * @return lista de hasta 5 productos con mejor relación calidad-precio
     */
    @Query("SELECT p FROM Product p WHERE p.status = true AND p.rating IS NOT NULL AND p.price > 0 ORDER BY (p.rating / p.price) DESC")
    List<Product> findTopBestQualityPriceRatio(Pageable pageable);

    /**
     * Busca productos por rango de rating con ordenación flexible.
     * Endpoint: GET /api/products/search/rating-range?ratingMin=...&ratingMax=...
     *
     * @param ratingMin rating mínimo (incluido)
     * @param ratingMax rating máximo (incluido)
     * @return lista de productos en rango de rating con status=true
     */
    @Query("SELECT p FROM Product p WHERE p.rating BETWEEN :ratingMin AND :ratingMax AND p.status = true")
    List<Product> findByRatingRangeAndStatusTrue(
            @Param("ratingMin") Double ratingMin,
            @Param("ratingMax") Double ratingMax);

    /**
     * Obtiene los 10 primeros productos nuevos con mejor valoración.
     * Endpoint: GET /api/products/search/top10-new-best-rated
     *
     * @return lista de hasta 10 productos nuevos ordenados por mejor rating
     */
    @Query("SELECT p FROM Product p WHERE p.condition = 'NOU' AND p.status = true ORDER BY p.rating DESC, p.id ASC LIMIT 10")
    List<Product> findTop10NewProductsBestRated();
}
