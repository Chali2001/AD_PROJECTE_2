package com.ra34.projecte2.mapper;

import com.ra34.projecte2.dto.CustomerCreateRequestDTO;
import com.ra34.projecte2.dto.UserCreateRequestDTO;
import com.ra34.projecte2.dto.RoleResponseDTO;
import com.ra34.projecte2.dto.UserResponseDTO;
import com.ra34.projecte2.model.Customer;
import com.ra34.projecte2.model.Role;
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
        response.setCustomer(CustomerMapper.toCustomerResponseDTO(user.getCustomer()));
        response.setRoles(user.getRoles().stream()
                .map(UserMapper::toRoleResponseDTO)
                .toList());
        return response;
    }

    public static RoleResponseDTO toRoleResponseDTO(Role role) {
        RoleResponseDTO response = new RoleResponseDTO();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        return response;
    }
}

