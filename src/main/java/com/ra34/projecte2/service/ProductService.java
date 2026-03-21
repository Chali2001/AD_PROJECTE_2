package com.ra34.projecte2.service;

import com.ra34.projecte2.dto.ProductResponseDTO;
import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.model.ProductCondition;
import com.ra34.projecte2.repository.ProductRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final int MAX_LIMIT = 100;
    private static final int EXPECTED_COLUMNS = 6;

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public int uploadCsv(MultipartFile file) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(br, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<CSVRecord> records = csvParser.getRecords();
            List<Product> batch = new java.util.ArrayList<>(records.size());

            for (CSVRecord record : records) {
                long lineNumber = record.getRecordNumber() + 1;
                if (record.size() != EXPECTED_COLUMNS) {
                    throw new RuntimeException("Error en la línea " + lineNumber
                            + ": se esperaban " + EXPECTED_COLUMNS + " columnas pero se encontraron " + record.size());
                }
                try {
                    Product p = new Product();
                    p.setName(record.get(0));
                    p.setDescription(record.get(1));
                    p.setStock(Integer.parseInt(record.get(2).trim()));
                    p.setPrice(Double.parseDouble(record.get(3).trim()));
                    p.setRating(Double.parseDouble(record.get(4).trim()));
                    p.setCondition(ProductCondition.valueOf(record.get(5).trim().toUpperCase()));
                    p.setStatus(true);
                    LocalDateTime now = LocalDateTime.now();
                    p.setDataCreated(now);
                    p.setDataUpdated(now);
                    batch.add(p);
                } catch (Exception e) {
                    throw new RuntimeException("Error en la línea " + lineNumber + ": " + e.getMessage(), e);
                }
            }
            productRepository.saveAll(batch);
            count = batch.size();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error procesando el archivo: " + e.getMessage(), e);
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
        dto.setCondition(p.getCondition());
        return dto;
    }

    // 1. Buscar por rango de precio y nombre (JPQL)
    public List<ProductResponseDTO> searchByPriceRangeAndName(Double priceMin, Double priceMax, String nameFragment, int limit) {
        int pageSize = Math.max(1, Math.min(limit, MAX_LIMIT));
        Pageable pageable = PageRequest.of(0, pageSize);
        List<Product> products = productRepository.findByPriceRangeAndNameAndStatusTrue(priceMin, priceMax, nameFragment, pageable);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 2. Top 5 mejor relación calidad-precio (JPQL)
    public List<ProductResponseDTO> getTop5BestQualityPrice() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Product> products = productRepository.findTopBestQualityPriceRatio(pageable);
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

}
