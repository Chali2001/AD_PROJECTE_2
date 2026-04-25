package com.ra34.projecte2.controller;

import com.ra34.projecte2.dto.AddressCreateRequestDTO;
import com.ra34.projecte2.dto.CustomerResponseDTO;
import com.ra34.projecte2.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Añade una lista de direcciones a un customer existente.
    @PostMapping("/{id}/addresses")
    public ResponseEntity<CustomerResponseDTO> addAddresses(@PathVariable Long id,
                                                            @RequestBody List<AddressCreateRequestDTO> requests) {

        CustomerResponseDTO response = customerService.addAddresses(id, requests);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }
}