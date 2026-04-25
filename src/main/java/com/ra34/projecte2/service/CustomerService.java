package com.ra34.projecte2.service;

import com.ra34.projecte2.dto.AddressCreateRequestDTO;
import com.ra34.projecte2.dto.CustomerResponseDTO;
import com.ra34.projecte2.mapper.CustomerMapper;
import com.ra34.projecte2.model.Address;
import com.ra34.projecte2.model.Customer;
import com.ra34.projecte2.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomer(Long customerId) {
        if (customerId == null) {
            return null;
        }

        return customerRepository.findById(customerId)
                .map(CustomerMapper::toCustomerResponseDTO)
                .orElse(null);
    }

    @Transactional
    public CustomerResponseDTO addAddresses(Long customerId, List<AddressCreateRequestDTO> requests) {
        if (customerId == null || requests == null || requests.isEmpty()) {
            return null;
        }

        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return null;
        }

        for (AddressCreateRequestDTO request : requests) {
            if (request == null || request.getAddress() == null || request.getCity() == null
                    || request.getPostalCode() == null || request.getCountry() == null) {
                return null;
            }

            Address address = CustomerMapper.toEntity(request);
            customer.addAddress(address);
        }

        customer.setDataUpdated(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.toCustomerResponseDTO(savedCustomer);
    }
}
