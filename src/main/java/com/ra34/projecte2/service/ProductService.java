package com.ra34.projecte2.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ra34.projecte2.dto.ProductDto;
import com.ra34.projecte2.dto.ProductInputDto;
import com.ra34.projecte2.dto.ProductResponseDTO;
import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.model.ProductCondition;
import com.ra34.projecte2.repository.ProductRepository;

/**
 * Servicio de productos que encapsula la lógica empresarial para operaciones CRUD
 * y búsquedas avanzadas. Utiliza ProductRepository para acceso a datos.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public int uploadCsv(MultipartFile file) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    continue;
                }

                String[] data = line.split(",");
                try {
                    Product p = new Product();
                    p.setName(data[0]);
                    p.setDescription(data[1]);
                    p.setStock(Integer.parseInt(data[2]));
                    p.setPrice(Double.parseDouble(data[3]));
                    p.setRating(Double.parseDouble(data[4]));
                    p.setCondition(ProductCondition.valueOf(data[5].toUpperCase()));
                    p.setStatus(true);
                    p.setDataCreated(LocalDateTime.now());
                    p.setDataUpdated(LocalDateTime.now());

                    productRepository.save(p);
                    count++;
                } catch (Exception e) {
                    throw new RuntimeException("Error en la línea " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error procesando el archivo: " + e.getMessage());
        }
        return count;
    }

    // MÉTODOS CRUD ADICIONALES

    /**
     * Obtiene todos los productos activos.
     *
     * @return lista de todos los productos activos
     */
    public List<ProductDto> findAllProducts() {
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getStatus() != null && p.getStatus())
                .map(this::mapProductDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un producto por ID.
     *
     * @param id ID del producto
     * @return ProductDto o null si no existe
     */
    public ProductDto findProductById(Long id) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("El ID debe ser válido y mayor que 0");
        }

        Optional<Product> product = productRepository.findById(id);
        return product.map(this::mapProductDto).orElse(null);
    }

    /**
     * Crea un nuevo producto.
     *
     * @param productInputDto DTO con datos del producto
     * @return DTO del producto creado
     */
    public ProductDto createProduct(ProductInputDto productInputDto) {
        if (productInputDto == null) {
            throw new IllegalArgumentException("Los datos del producto no pueden ser nulos");
        }

        // Validar campos requeridos
        if (productInputDto.getName() == null || productInputDto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        if (productInputDto.getName().length() > 20) {
            throw new IllegalArgumentException("El nombre no puede exceder 20 caracteres");
        }

        if (productInputDto.getDescription() != null && productInputDto.getDescription().length() > 100) {
            throw new IllegalArgumentException("La descripción no puede exceder 100 caracteres");
        }

        if (productInputDto.getStock() == null || productInputDto.getStock() < 0) {
            throw new IllegalArgumentException("El stock es obligatorio y no puede ser negativo");
        }

        if (productInputDto.getPrice() == null || productInputDto.getPrice() < 0) {
            throw new IllegalArgumentException("El precio es obligatorio y no puede ser negativo");
        }

        // Crear producto
        Product product = new Product();
        product.setName(productInputDto.getName());
        product.setDescription(productInputDto.getDescription());
        product.setStock(productInputDto.getStock());
        product.setPrice(productInputDto.getPrice());
        product.setRating(productInputDto.getRating());
        product.setCondition(productInputDto.getCondition());
        product.setStatus(true);
        product.setDataCreated(LocalDateTime.now());
        product.setDataUpdated(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        return mapProductDto(savedProduct);
    }

    /**
     * Actualiza todos los campos de un producto existente.
     *
     * @param id ID del producto
     * @param productInputDto DTO con nuevos datos
     * @return DTO del producto actualizado o null si no existe
     */
    public ProductDto updateProduct(Long id, ProductInputDto productInputDto) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("El ID debe ser válido y mayor que 0");
        }

        if (productInputDto == null) {
            throw new IllegalArgumentException("Los datos del producto no pueden ser nulos");
        }

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return null;
        }

        Product product = optionalProduct.get();

        // Validar y actualizar campos
        if (productInputDto.getName() != null) {
            if (productInputDto.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
            }
            if (productInputDto.getName().length() > 20) {
                throw new IllegalArgumentException("El nombre no puede exceder 20 caracteres");
            }
            product.setName(productInputDto.getName());
        }

        if (productInputDto.getDescription() != null && productInputDto.getDescription().length() > 100) {
            throw new IllegalArgumentException("La descripción no puede exceder 100 caracteres");
        }
        if (productInputDto.getDescription() != null) {
            product.setDescription(productInputDto.getDescription());
        }

        if (productInputDto.getStock() != null) {
            if (productInputDto.getStock() < 0) {
                throw new IllegalArgumentException("El stock no puede ser negativo");
            }
            product.setStock(productInputDto.getStock());
        }

        if (productInputDto.getPrice() != null) {
            if (productInputDto.getPrice() < 0) {
                throw new IllegalArgumentException("El precio no puede ser negativo");
            }
            product.setPrice(productInputDto.getPrice());
        }

        if (productInputDto.getRating() != null) {
            product.setRating(productInputDto.getRating());
        }

        if (productInputDto.getCondition() != null) {
            product.setCondition(productInputDto.getCondition());
        }

        product.setDataUpdated(LocalDateTime.now());
        Product updatedProduct = productRepository.save(product);
        return mapProductDto(updatedProduct);
    }

    // BÚSQUEDAS POR NOMBRE (QUERY DERIVATION) 

    /**
     * Busca productos cuyo nombre contiene el prefijo especificado.
     * Endpoint: GET /api/products/search/name?prefix=...
     *
     * @param prefix prefijo a buscar en el nombre
     * @return lista de ProductDto que contienen el prefijo
     */
    public List<ProductDto> findByNamePrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("El prefijo de búsqueda no puede estar vacío");
        }

        return productRepository.findByNameContainingAndStatusTrue(prefix)
                .stream()
                .map(this::mapProductDto)
                .collect(Collectors.toList());
    }

    // BÚSQUEDAS AVANZADAS JPQL

    /**
     * Busca productos por rango de precios y nombre.
     * Integrant 1, Endpoint: GET /api/products/search/price-range
     *
     * @param priceMin precio mínimo (incluido)
     * @param priceMax precio máximo (incluido)
     * @param prefix prefijo del nombre
     * @param limit número máximo de resultados
     * @return lista de ProductResponseDTO
     */
    public List<ProductResponseDTO> searchByPriceRangeAndName(Double priceMin, Double priceMax, String prefix, int limit) {
        if (priceMin == null || priceMax == null) {
            throw new IllegalArgumentException("priceMin y priceMax são obligatorios");
        }

        if (priceMin > priceMax) {
            throw new IllegalArgumentException("priceMin no puede ser mayor que priceMax");
        }

        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("El prefijo no puede estar vacío");
        }

        if (limit <= 0) {
            throw new IllegalArgumentException("El limit debe ser mayor que 0");
        }

        Pageable pageable = PageRequest.of(0, limit);
        List<Product> products = productRepository.findByPriceRangeAndNameAndStatusTrue(priceMin, priceMax, prefix, pageable);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Obtiene los mejores 5 productos por relación calidad-precio.
     * Integrant 1, Endpoint: GET /api/products/search/top5-quality-price
     *
     * @return lista de hasta 5 ProductResponseDTO ordenados por relación calidad-precio
     */
    public List<ProductResponseDTO> getTop5BestQualityPrice() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Product> products = productRepository.findTopBestQualityPriceRatio(pageable);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Busca productos por rango de rating con ordenación flexible.
     * Integrant 2, Endpoint: GET /api/products/search/rating-range
     *
     * @param ratingMin rating mínimo (incluido)
     * @param ratingMax rating máximo (incluido)
     * @param camp campo para ordenar (rating o price)
     * @param order dirección de ordenación (asc o desc)
     * @param limit número máximo de resultados
     * @return lista de ProductDto ordenada según parámetros
     */
    public List<ProductDto> searchByRatingRange(Double ratingMin, Double ratingMax, String camp, String order, Integer limit) {
        if (ratingMin == null || ratingMax == null) {
            throw new IllegalArgumentException("ratingMin y ratingMax son obligatorios");
        }

        if (ratingMin > ratingMax) {
            throw new IllegalArgumentException("ratingMin no puede ser mayor que ratingMax");
        }

        if (camp == null || camp.trim().isEmpty()) {
            throw new IllegalArgumentException("El parámetro camp es obligatorio");
        }

        if (order == null || order.trim().isEmpty()) {
            throw new IllegalArgumentException("El parámetro order es obligatorio");
        }

        if (limit == null || limit <= 0) {
            throw new IllegalArgumentException("El limit debe ser mayor que 0");
        }

        List<Product> products = productRepository.findByRatingRangeAndStatusTrue(ratingMin, ratingMax);

        // Aplicar ordenación
        Comparator<Product> comparator;
        if (camp.equalsIgnoreCase("rating")) {
            comparator = Comparator.comparing(Product::getRating, Comparator.nullsLast(Double::compareTo));
        } else if (camp.equalsIgnoreCase("price")) {
            comparator = Comparator.comparing(Product::getPrice);
        } else {
            throw new IllegalArgumentException("El parámetro camp debe ser 'rating' o 'price'");
        }

        if (order.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        } else if (!order.equalsIgnoreCase("asc")) {
            throw new IllegalArgumentException("El parámetro order debe ser 'asc' o 'desc'");
        }

        return products.stream()
                .sorted(comparator)
                .limit(limit)
                .map(this::mapProductDto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los 10 primeros productos nuevos con mejor valoración.
     * Integrant 2, Endpoint: GET /api/products/search/top10-new-best-rated
     *
     * @return lista de hasta 10 ProductDto con productos nuevos y mejor rating
     */
    public List<ProductDto> findTop10NewBestRated() {
        return productRepository.findTop10ByConditionAndStatusTrueOrderByRatingDesc(ProductCondition.NOU)
                .stream()
                .limit(10)
                .map(this::mapProductDto)
                .collect(Collectors.toList());
    }

    //  MÉTODOS AUXILIARES DE MAPEO 

    /**
     * Convierte una entidad Product a ProductDto (sin campos de auditoría ni status).
     *
     * @param p producto a convertir
     * @return ProductDto con datos públicos
     */
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

    /**
     * Convierte una entidad Product a ProductResponseDTO (para respuestas JPQL).
     *
     * @param p producto a convertir
     * @return ProductResponseDTO con datos públicos
     */
    private ProductResponseDTO convertToDTO(Product p) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setStock(p.getStock());
        dto.setPrice(p.getPrice());
        dto.setRating(p.getRating());
        dto.setConditionStatus(p.getCondition());
        return dto;
    }

    // MÉTODOS DE CONSULTA HEREDADOS 

    /**
     * Busca productos por condición que estén activos.
     *
     * @param condition condición del producto
     * @return lista de ProductDto con esa condición
     */
    public List<ProductDto> findByCondition(String condition) {
        ProductCondition productCondition = ProductCondition.valueOf(condition.toUpperCase());
        List<Product> products = productRepository.findByConditionAndStatusTrue(productCondition);
        return products.stream().map(this::mapProductDto).toList();
    }

    /**
     * Obtiene productos ordenados por rating en dirección especificada.
     *
     * @param order "asc" para ascendente, "desc" para descendente
     * @return lista de ProductDto ordenada por rating
     */
    public List<ProductDto> findByOrderRating(String order) {
        if (order == null) {
            throw new IllegalArgumentException("El parámetro order es obligatorio");
        }

        if (order.equalsIgnoreCase("asc")) {
            return productRepository.findByStatusTrueOrderByRatingAsc().stream().map(this::mapProductDto).toList();
        }

        if (order.equalsIgnoreCase("desc")) {
            return productRepository.findByStatusTrueOrderByRatingDesc().stream().map(this::mapProductDto).toList();
        }

        throw new IllegalArgumentException("El parámetro order debe ser asc o desc");
    }

    /**
     * Obtiene productos con paginación (5 por página).
     *
     * @param page número de página (0-based)
     * @return lista de ProductDto paginada
     */
    public List<ProductDto> findProductsPage(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("page no puede ser negativo");
        }

        Pageable pageable = PageRequest.of(page, 5);
        return productRepository.findByStatusTrue(pageable).getContent().stream().map(this::mapProductDto).toList();
    }

    /**
     * Actualiza el stock de un producto.
     *
     * @param id ID del producto
     * @param stock nuevo valor de stock
     * @return producto actualizado o null si no existe
     */
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

    /**
     * Actualiza el precio de un producto.
     *
     * @param id ID del producto
     * @param price nuevo valor de precio
     * @return producto actualizado o null si no existe
     */
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

    /**
     * Elimina un producto de forma física (de la base de datos).
     *
     * @param id ID del producto a eliminar
     * @return 1 si se eliminó, 0 si no existe
     */
    public int deleteProductPhysical(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return 0;
        }

        productRepository.deleteById(id);
        return 1;
    }

    /**
     * Elimina un producto de forma lógica (marca status=false).
     *
     * @param id ID del producto
     * @return producto actualizado o null si no existe
     */
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
