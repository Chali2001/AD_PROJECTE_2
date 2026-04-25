package com.ra34.projecte2.service;

import com.ra34.projecte2.dto.UserCreateRequestDTO;
import com.ra34.projecte2.dto.UserResponseDTO;
import com.ra34.projecte2.mapper.UserMapper;
import com.ra34.projecte2.model.Customer;
import com.ra34.projecte2.model.User;
import com.ra34.projecte2.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDTO createUser(UserCreateRequestDTO request) {
        if (request == null || request.getCustomer() == null) {
            return null;
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return null;
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return null;
        }
        // Si el email ya existe, no se crea ni user ni customer.
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            return null;
        }

        User user = UserMapper.toEntity(request);
        LocalDateTime now = LocalDateTime.now();

        // Campos de auditoría y estado definidos en servicio.
        user.setEmail(request.getEmail().trim());
        user.setStatus(true);
        user.setDataCreated(now);
        user.setDataUpdated(now);

        Customer customer = user.getCustomer();
        customer.setStatus(true);
        customer.setDataCreated(now);
        customer.setDataUpdated(now);

        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(savedUser);
    }
}
