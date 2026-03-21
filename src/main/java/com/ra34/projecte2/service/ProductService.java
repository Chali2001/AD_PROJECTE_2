package com.ra34.projecte2.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ra34.projecte2.dto.ProductDto;
import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.model.ProductCondition;
import com.ra34.projecte2.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	// Mapeja Product a ProductDto per no retornar auditoria ni status
	public ProductDto mapProductDto(Product p) {
		return new ProductDto(
				p.getId(),
				p.getName(),
				p.getDescription(),
				p.getStock(),
				p.getPrice(),
				p.getRating(),
				p.getCondition());
	}

	// Cerca per condició, només productes actius
	public List<ProductDto> findByCondition(String condition) {
		ProductCondition productCondition = ProductCondition.valueOf(condition.toUpperCase());
		List<Product> products = productRepository.findByConditionAndStatusTrue(productCondition);
		return products.stream().map(p -> mapProductDto(p)).toList();
	}

	// Ordena per rating (asc/desc), només productes actius
	public List<ProductDto> findByOrderRating(String order) {
		if (order == null) {
			throw new IllegalArgumentException("El parámetro order es obligatorio");
		}

		if (order.equalsIgnoreCase("asc")) {
			List<Product> products = productRepository.findByStatusTrueOrderByRatingAsc();
			return products.stream().map(p -> mapProductDto(p)).toList();
		}

		if (order.equalsIgnoreCase("desc")) {
			List<Product> products = productRepository.findByStatusTrueOrderByRatingDesc();
			return products.stream().map(p -> mapProductDto(p)).toList();
		}

		throw new IllegalArgumentException("El parámetro order debe ser asc o desc");
	}

		// Cerca avançada per rang de rating, ordenació i límit
		public List<ProductDto> findByRatingRangeOrder(Double ratingMin, Double ratingMax, String camp, String order,
				Integer limit) {
			if (ratingMin == null || ratingMax == null) {
				throw new IllegalArgumentException("ratingMin y ratingMax son obligatorios");
			}

			if (limit == null || limit <= 0) {
				throw new IllegalArgumentException("limit debe ser mayor que 0");
			}

			if (camp == null || order == null) {
				throw new IllegalArgumentException("camp y order son obligatorios");
			}

			List<Product> products = productRepository.findByRatingBetweenAndStatusTrue(ratingMin, ratingMax);

			Comparator<Product> comparator;
			if (camp.equalsIgnoreCase("rating")) {
				comparator = Comparator.comparing(Product::getRating, Comparator.nullsLast(Double::compareTo));
			} else if (camp.equalsIgnoreCase("price")) {
				comparator = Comparator.comparing(Product::getPrice);
			} else {
				throw new IllegalArgumentException("El parámetro camp debe ser rating o price");
			}

			if (order.equalsIgnoreCase("desc")) {
				comparator = comparator.reversed();
			} else if (!order.equalsIgnoreCase("asc")) {
				throw new IllegalArgumentException("El parámetro order debe ser asc o desc");
			}

			return products.stream()
					.sorted(comparator)
					.limit(limit)
					.map(p -> mapProductDto(p))
					.toList();
		}

		// Top 10 productes nous amb millor valoració
		public List<ProductDto> findTop10NewBestRated() {
			List<Product> products = productRepository.findTop10ByConditionAndStatusTrueOrderByRatingDesc(ProductCondition.NOU);
			return products.stream().map(p -> mapProductDto(p)).toList();
		}

    // Mètode per actualitzar el stock d'un producte
	public Product updateStock(Long id, Integer stock) {
		if (stock == null || stock < 0) {
			throw new IllegalArgumentException("El stock no puede ser null ni negativo");
		}

		Product product = productRepository.findById(id).orElse(null);
		if (product == null) {
			return null;
		}

		product.setStock(stock);
		product.setDataUpdated(LocalDateTime.now());
		return productRepository.save(product);
	}

    // Mètode per actualitzar el preu d'un producte
	public Product updatePrice(Long id, Double price) {
		if (price == null || price < 0) {
			throw new IllegalArgumentException("El precio no puede ser null ni negativo");
		}

		Product product = productRepository.findById(id).orElse(null);
		if (product == null) {
			return null;
		}

		product.setPrice(price);
		product.setDataUpdated(LocalDateTime.now());
		return productRepository.save(product);
	}

    // Mètode per eliminar un producte de forma física
	public int deleteProductPhysical(Long id) {
		Product product = productRepository.findById(id).orElse(null);
		if (product == null) {
			return 0;
		}

		productRepository.deleteById(id);
		return 1;
	}

    // Mètode per eliminar un producte de forma lògica
	public Product deleteProductLogical(Long id) {
		Product product = productRepository.findById(id).orElse(null);
		if (product == null) {
			return null;
		}

		product.setStatus(false);
		product.setDataUpdated(LocalDateTime.now());
		return productRepository.save(product);
	}

}
