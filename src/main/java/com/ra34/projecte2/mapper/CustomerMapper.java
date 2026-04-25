package com.ra34.projecte2.mapper;

import com.ra34.projecte2.dto.AddressCreateRequestDTO;
import com.ra34.projecte2.dto.AddressResponseDTO;
import com.ra34.projecte2.dto.CustomerResponseDTO;
import com.ra34.projecte2.model.Address;
import com.ra34.projecte2.model.Customer;

import java.util.List;
import java.util.stream.Collectors;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    public static Address toEntity(AddressCreateRequestDTO request) {
        Address address = new Address();
        address.setAddress(request.getAddress());
        address.setCity(request.getCity());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setIsDefault(request.getIsDefault());
        return address;
    }

    public static AddressResponseDTO toAddressResponseDTO(Address address) {
        AddressResponseDTO response = new AddressResponseDTO();
        response.setId(address.getId());
        response.setAddress(address.getAddress());
        response.setCity(address.getCity());
        response.setPostalCode(address.getPostalCode());
        response.setCountry(address.getCountry());
        response.setIsDefault(address.getIsDefault());
        return response;
    }

    public static CustomerResponseDTO toCustomerResponseDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(customer.getId());
        if (customer.getUser() != null) {
            response.setEmail(customer.getUser().getEmail());
        }
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setPhone(customer.getPhone());
        response.setAddresses(customer.getAddresses().stream()
                .map(CustomerMapper::toAddressResponseDTO)
                .collect(Collectors.toList()));
        return response;
    }

    public static List<Address> toAddressEntities(List<AddressCreateRequestDTO> requests) {
        return requests.stream()
                .map(CustomerMapper::toEntity)
                .collect(Collectors.toList());
    }
}
