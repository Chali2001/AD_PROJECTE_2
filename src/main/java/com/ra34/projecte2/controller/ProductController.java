package com.ra34.projecte2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ra34.projecte2.dto.ErrorDto;
import com.ra34.projecte2.dto.ProductDto;
import com.ra34.projecte2.dto.ProductInputDto;
import com.ra34.projecte2.dto.ProductResponseDTO;
import com.ra34.projecte2.dto.UpdatePriceDto;
import com.ra34.projecte2.dto.UpdateStockDto;
import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.service.ProductService;

/**
 * Controlador REST para gestionar operaciones de productos.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	// ENDPOINTS CRUD

	/**
	 * Obtiene todos los productos activos.
	 * Endpoint: GET /api/products
	 *
	 * @return lista de ProductDto activos
	 */
	@GetMapping
	public ResponseEntity<?> getAllProducts() {
		try {
			List<ProductDto> products = productService.findAllProducts();
			return ResponseEntity.status(HttpStatus.OK).body(products);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

	/**
	 * Obtiene un producto específico por ID.
	 * Endpoint: GET /api/products/{id}
	 *
	 * @param id ID del producto
	 * @return ProductDto si existe, error 404 si no
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getProductById(@PathVariable Long id) {
		try {
			ProductDto product = productService.findProductById(id);
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

	/**
	 * Crear un nuevo producto.
	 * Endpoint: POST /api/products
	 *
	 * @param productInputDto datos del nuevo producto
	 * @return ProductDto creado
	 */
	@PostMapping
	public ResponseEntity<?> createProduct(@RequestBody ProductInputDto productInputDto) {
		try {
			ProductDto newProduct = productService.createProduct(productInputDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

	/**
	 * Actualizar todos los campos de un producto existente.
	 * Endpoint: PUT /api/products/{id}
	 *
	 * @param id ID del producto a actualizar
	 * @param productInputDto nuevos datos del producto
	 * @return ProductDto actualizado
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductInputDto productInputDto) {
		try {
			ProductDto updatedProduct = productService.updateProduct(id, productInputDto);
			if (updatedProduct == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ErrorDto(HttpStatus.NOT_FOUND.value(), "Producto no encontrado"));
			}
			return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

	/**
	 * Actualizar solo el stock de un producto.
	 * Endpoint: PATCH /api/products/{id}/stock
	 *
	 * @param id ID del producto
	 * @param dto DTO con el nuevo stock
	 * @return producto actualizado
	 */
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

	/**
	 * Actualizar solo el precio de un producto.
	 * Endpoint: PATCH /api/products/{id}/price
	 *
	 * @param id ID del producto
	 * @param dto DTO con el nuevo precio
	 * @return producto actualizado
	 */
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

	/**
	 * Eliminar un producto de la base de datos
	 * Endpoint: DELETE /api/products/{id}
	 *
	 * @param id ID del producto a eliminar
	 * @return response sin contenido
	 */
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

	/**
	 * Eliminar un producto de forma lógica (marca status=false).
	 * Endpoint: PATCH /api/products/{id}/logical-delete
	 *
	 * @param id ID del producto a eliminar lógicamente
	 * @return producto actualizado
	 */
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

	// ENDPOINTS DE CARGA MASIVA

	/**
	 * Carga masiva de productos desde un archivo CSV.
	 * Endpoint: POST /api/products/upload
	 * 
	 * El archivo debe tener cabecera y columnas separadas por coma:
	 * name, description, stock, price, rating, condition
	 *
	 * @param file archivo CSV a procesar
	 * @return número de registros cargados o error
	 */
	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
		try {
			int guardados = productService.uploadCsv(file);
			return ResponseEntity.ok("S'han afegit " + guardados + " registres a la base de dades.");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		}
	}

	// ENDPOINTS DE BÚSQUEDA SIMPLE (QUERY DERIVATION) 

	/**
	 * Buscar productos por nombre (búsqueda parcial).
	 * Endpoint: GET /api/products/search/name?prefix=...
	 * 
	 * Solo retorna productos activos (status=true).
	 * No incluye campos de auditoría ni status.
	 *
	 * @param prefix prefijo a buscar en el nombre
	 * @return lista de ProductDto
	 */
	@GetMapping("/search/name")
	public ResponseEntity<?> findByName(@RequestParam String prefix) {
		try {
			List<ProductDto> products = productService.findByNamePrefix(prefix);
			return ResponseEntity.status(HttpStatus.OK).body(products);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

	/**
	 * Buscar productos por condición.
	 * Endpoint: GET /api/products/search/condition?condition=...
	 * 
	 * Solo retorna productos activos (status=true).
	 * Condiciones válidas: NOU, BON_ESTAT, ACCEPTABLE, MAL_ESTAT
	 *
	 * @param condition condición a buscar
	 * @return lista de ProductDto
	 */
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

	/**
	 * Ordenar productos por rating.
	 * Endpoint: GET /api/products/search/order?camp=rating&order=asc
	 * 
	 * Parámetros:
	 * - camp: solo soporta "rating"
	 * - order: "asc" para ascendente, "desc" para descendente
	 *
	 * @param camp campo por el cual ordenar
	 * @param order dirección de ordenación
	 * @return lista de ProductDto ordenada
	 */
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

	// ENDPOINTS DE PAGINACIÓN 

	/**
	 * Obtener productos con paginación (5 por página).
	 * Endpoint: GET /api/products/search/page?page=0
	 * 
	 * Solo retorna productos activos (status=true).
	 * No incluye campos de auditoría ni status.
	 *
	 * @param page número de página (0-based, por defecto 0)
	 * @return lista de ProductDto paginada
	 */
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

	// ENDPOINTS DE BÚSQUEDA AVANZADA (JPQL)

	/**
	 * Buscar productos por rango de precios (JPQL).
	 * Integrant 1, Endpoint: GET /api/products/search/price-range
	 * 
	 * Parámetros:
	 * - priceMin: precio mínimo (incluido)
	 * - priceMax: precio máximo (incluido)
	 * - prefix: prefijo del nombre
	 * - limit: número máximo de resultados (por defecto 10)
	 *
	 * @param priceMin precio mínimo
	 * @param priceMax precio máximo
	 * @param prefix prefijo del nombre
	 * @param limit número máximo de resultados
	 * @return lista de ProductResponseDTO
	 */
	@GetMapping("/search/price-range")
	public ResponseEntity<?> searchByPriceRange(
			@RequestParam Double priceMin,
			@RequestParam Double priceMax,
			@RequestParam String prefix,
			@RequestParam(defaultValue = "10") int limit) {
		try {
			List<ProductResponseDTO> results = productService.searchByPriceRangeAndName(priceMin, priceMax, prefix, limit);
			return ResponseEntity.ok(results);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

	/**
	 * Obtener los 5 mejores productos por relación calidad-precio (JPQL).
	 * Integrant 1, Endpoint: GET /api/products/search/top5-quality-price
	 * 
	 * Calcula: rating / price para cada producto
	 * Solo incluye productos activos (status=true).
	 *
	 * @return lista de hasta 5 ProductResponseDTO ordenados por mejor relación
	 */
	@GetMapping("/search/top5-quality-price")
	public ResponseEntity<?> getTop5QualityPrice() {
		try {
			List<ProductResponseDTO> results = productService.getTop5BestQualityPrice();
			return ResponseEntity.ok(results);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

	/**
	 * Buscar productos por rango de rating con ordenación flexible (JPQL).
	 * Integrant 2, Endpoint: GET /api/products/search/rating-range
	 * 
	 * Parámetros:
	 * - ratingMin: rating mínimo (incluido)
	 * - ratingMax: rating máximo (incluido)
	 * - camp: campo para ordenar ("rating" o "price")
	 * - order: dirección ("asc" o "desc")
	 * - limit: número máximo de resultados
	 *
	 * @param ratingMin rating mínimo
	 * @param ratingMax rating máximo
	 * @param camp campo para ordenar
	 * @param order dirección de ordenación
	 * @param limit número máximo de resultados
	 * @return lista de ProductDto
	 */
	@GetMapping("/search/rating-range")
	public ResponseEntity<?> searchByRatingRange(
			@RequestParam Double ratingMin,
			@RequestParam Double ratingMax,
			@RequestParam String camp,
			@RequestParam String order,
			@RequestParam Integer limit) {
		try {
			List<ProductDto> results = productService.searchByRatingRange(ratingMin, ratingMax, camp, order, limit);
			return ResponseEntity.ok(results);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

	/**
	 * Obtener los 10 mejores productos nuevos por valoración (JPQL).
	 * Integrant 2, Endpoint: GET /api/products/search/top10-new-best-rated
	 * 
	 * Solo retorna productos con condición "NOU" y status=true.
	 * Ordenados por rating descendente.
	 *
	 * @return lista de hasta 10 ProductDto
	 */
	@GetMapping("/search/top10-new-best-rated")
	public ResponseEntity<?> getTop10NewBestRated() {
		try {
			List<ProductDto> results = productService.findTop10NewBestRated();
			return ResponseEntity.ok(results);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error no controlado"));
		}
	}

}
