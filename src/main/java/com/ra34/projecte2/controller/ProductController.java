package com.ra34.projecte2.controller;

import com.ra34.projecte2.dto.ErrorDTO;
import com.ra34.projecte2.dto.ProductResponseDTO;
import com.ra34.projecte2.service.ProductService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Endpoint 2: Carga masiva CSV 
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            int guardados = productService.uploadCsv(file);
            return ResponseEntity.ok("S'han afegit " + guardados + " registres a la base de dades.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorDTO("ERROR_CSV", e.getMessage()));
        }
    }


    // [Endpoint JPQL] Buscar por rango de precios y prefijo 
    @GetMapping("/search/range")
    public ResponseEntity<List<ProductResponseDTO>> searchByPriceRange(
            @RequestParam Double priceMin,
            @RequestParam Double priceMax,
            @RequestParam String prefix,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<ProductResponseDTO> results = productService.searchByPriceRangeAndName(priceMin, priceMax, prefix, limit);
        return ResponseEntity.ok(results);
    }

    // [Endpoint JPQL] Top 5 mejor relación calidad-precio 
    @GetMapping("/search/top5-quality-price")
    public ResponseEntity<List<ProductResponseDTO>> getTop5QualityPrice() {
        List<ProductResponseDTO> results = productService.getTop5BestQualityPrice();
        return ResponseEntity.ok(results);
    }

}