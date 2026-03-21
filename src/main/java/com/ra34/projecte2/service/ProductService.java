package com.ra34.projecte2.service;

import java.time.LocalDateTime;
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
