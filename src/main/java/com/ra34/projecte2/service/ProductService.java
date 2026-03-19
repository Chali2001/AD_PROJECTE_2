package com.ra34.projecte2.service;

import com.ra34.projecte2.dto.ProductResponseDTO;
import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.model.ProductCondition;
import com.ra34.projecte2.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository; // Inyección por constructor

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public int uploadCsv(MultipartFile file) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) continue; // Saltar cabecera
                
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
    // 1. Buscar por rango de precio y nombre (JPQL) 
    public List<ProductResponseDTO> searchByPriceRangeAndName(Double priceMin, Double priceMax, String prefix, int limit) {
        // Usamos PageRequest para limitar el número de resultados (paginación básica)
        Pageable pageable = PageRequest.of(0, limit);
        List<Product> products = productRepository.findByPriceRangeAndNameAndStatusTrue(priceMin, priceMax, prefix, pageable);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 2. Top 5 mejor relación calidad-precio (JPQL) 
    public List<ProductResponseDTO> getTop5BestQualityPrice() {
        // Pedimos solo la primera página con un tamaño máximo de 5 elementos
        Pageable pageable = PageRequest.of(0, 5);
        List<Product> products = productRepository.findTopBestQualityPriceRatio(pageable);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}