package com.ra34.projecte2.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ra34.projecte2.dto.ProductDto;
import com.ra34.projecte2.dto.ProductResponseDTO;
import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.model.ProductCondition;
import com.ra34.projecte2.repository.ProductRepository;

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

    // Función traductora para ocultar datos de auditoría
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

    // Buscar por rango de precio y nombre (JPQL)
    public List<ProductResponseDTO> searchByPriceRangeAndName(Double priceMin, Double priceMax, String prefix, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Product> products = productRepository.findByPriceRangeAndNameAndStatusTrue(priceMin, priceMax, prefix, pageable);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Top 5 mejor relación calidad-precio (JPQL)
    public List<ProductResponseDTO> getTop5BestQualityPrice() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Product> products = productRepository.findTopBestQualityPriceRatio(pageable);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Cerca per condició, només productes actius
    public List<ProductDto> findByCondition(String condition) {
        ProductCondition productCondition = ProductCondition.valueOf(condition.toUpperCase());
        List<Product> products = productRepository.findByConditionAndStatusTrue(productCondition);
        return products.stream().map(this::mapProductDto).toList();
    }

    // Ordena per rating (asc/desc), només productes actius
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

        return products.stream().sorted(comparator).limit(limit).map(this::mapProductDto).toList();
    }

    // Top 10 productes nous amb millor valoració
    public List<ProductDto> findTop10NewBestRated() {
        return productRepository.findTop10ByConditionAndStatusTrueOrderByRatingDesc(ProductCondition.NOU)
                .stream()
                .map(this::mapProductDto)
                .toList();
    }

    // Paginació de 5 productes per pàgina
    public List<ProductDto> findProductsPage(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("page no puede ser negativo");
        }

        Pageable pageable = PageRequest.of(page, 5);
        return productRepository.findByStatusTrue(pageable).getContent().stream().map(this::mapProductDto).toList();
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
