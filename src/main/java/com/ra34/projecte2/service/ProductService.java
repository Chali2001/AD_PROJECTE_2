package com.ra34.projecte2.service;

import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.model.ProductCondition;
import com.ra34.projecte2.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

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
}