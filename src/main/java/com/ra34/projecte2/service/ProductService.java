package com.ra34.projecte2.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

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
