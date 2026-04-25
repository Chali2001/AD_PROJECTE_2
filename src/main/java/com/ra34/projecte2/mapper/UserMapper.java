package com.ra34.projecte2.mapper;

import com.ra34.projecte2.dto.CustomerCreateRequestDTO;
import com.ra34.projecte2.dto.UserCreateRequestDTO;
import com.ra34.projecte2.dto.UserResponseDTO;
import com.ra34.projecte2.model.Customer;
import com.ra34.projecte2.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserCreateRequestDTO request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        Customer customer = toCustomerEntity(request.getCustomer());
        user.setCustomer(customer);
        return user;
    }

    public static Customer toCustomerEntity(CustomerCreateRequestDTO customerRequest) {
        Customer customer = new Customer();
        if (customerRequest != null) {
            customer.setFirstName(customerRequest.getFirstName());
            customer.setLastName(customerRequest.getLastName());
            customer.setPhone(customerRequest.getPhone());
        }
        return customer;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        return response;
    }
}
