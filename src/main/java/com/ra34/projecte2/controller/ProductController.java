package com.ra34.projecte2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ra34.projecte2.dto.ErrorDto;
import com.ra34.projecte2.dto.ProductDto;
import com.ra34.projecte2.dto.UpdatePriceDto;
import com.ra34.projecte2.dto.UpdateStockDto;
import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	// Endpoint per paginació de 5 productes per bloque
	@GetMapping("/search/page")
	public ResponseEntity<?> findProductsPage(@RequestParam(defaultValue = "0") int page) {
		try {
			List<ProductDto> products = productService.findProductsPage(page);
			return ResponseEntity.status(HttpStatus.OK).body(products);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

	// Endpoint per consultar productes per condició i status=true
	@GetMapping("/search/condition")
	public ResponseEntity<?> findByCondition(@RequestParam String condition) {
		try {
			List<ProductDto> products = productService.findByCondition(condition);
			return ResponseEntity.status(HttpStatus.OK).body(products);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), "Condition no válida"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

	// Endpoint per ordenar productes per rating (asc/desc) i status=true
	@GetMapping("/search/order")
	public ResponseEntity<?> findByOrder(@RequestParam String camp, @RequestParam String order) {
		try {
			if (!camp.equalsIgnoreCase("rating")) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), "El parámetro camp debe ser rating"));
			}

			List<ProductDto> products = productService.findByOrderRating(order);
			return ResponseEntity.status(HttpStatus.OK).body(products);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

    // Endpoint per actualitzar el stock d'un producte
	@PatchMapping("/{id}/stock")
	public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestBody UpdateStockDto dto) {
		try {
			Product product = productService.updateStock(id, dto.getStock());
			if (product == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorDto(HttpStatus.NOT_FOUND.value(), "Producto no encontrado"));
			}

			return ResponseEntity.status(HttpStatus.OK).body(product);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

    // Endpoint per actualitzar el preu d'un producte
	@PatchMapping("/{id}/price")
	public ResponseEntity<?> updatePrice(@PathVariable Long id, @RequestBody UpdatePriceDto dto) {
		try {
			Product product = productService.updatePrice(id, dto.getPrice());
			if (product == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorDto(HttpStatus.NOT_FOUND.value(), "Producto no encontrado"));
			}

			return ResponseEntity.status(HttpStatus.OK).body(product);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

    // Endpoint per eliminar un producte de forma física
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePhysical(@PathVariable Long id) {
		try {
			int deleted = productService.deleteProductPhysical(id);
			if (deleted == 0) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorDto(HttpStatus.NOT_FOUND.value(), "Producto no encontrado"));
			}

			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

    // Endpoint per eliminar un producte de forma lògica
	@PatchMapping("/{id}/logical-delete")
	public ResponseEntity<?> deleteLogical(@PathVariable Long id) {
		try {
			Product product = productService.deleteProductLogical(id);
			if (product == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorDto(HttpStatus.NOT_FOUND.value(), "Producto no encontrado"));
			}

			return ResponseEntity.status(HttpStatus.OK).body(product);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

}
