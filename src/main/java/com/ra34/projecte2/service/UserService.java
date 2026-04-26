package com.ra34.projecte2.service;

import com.ra34.projecte2.dto.UserCreateRequestDTO;
import com.ra34.projecte2.dto.UserAddRolesRequestDTO;
import com.ra34.projecte2.dto.UserResponseDTO;
import com.ra34.projecte2.mapper.UserMapper;
import com.ra34.projecte2.model.Customer;
import com.ra34.projecte2.model.Role;
import com.ra34.projecte2.model.User;
import com.ra34.projecte2.repository.RoleRepository;
import com.ra34.projecte2.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

    @Transactional(readOnly = true)
    public UserResponseDTO getUser(Long id) {
        if (id == null) {
            return null;
        }

        return userRepository.findById(id)
                .map(UserMapper::toResponseDTO)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsers() {
        // Listado general de usuarios devolviendo solo DTOs.
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO addRoles(Long userId, UserAddRolesRequestDTO request) {
        if (userId == null || request == null || request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            return null;
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        for (Long roleId : request.getRoleIds()) {
            if (roleId == null) {
                return null;
            }

            Role role = roleRepository.findById(roleId).orElse(null);
            if (role == null) {
                return null;
            }

            if (!user.getRoles().contains(role)) {
                user.getRoles().add(role);
            }
        }

        user.setDataUpdated(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserCreateRequestDTO request) {
        if (id == null || request == null) {
            return null;
        }

        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String newEmail = request.getEmail().trim();
            if (!newEmail.equalsIgnoreCase(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
                return null;
            }
            user.setEmail(newEmail);
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }

        if (request.getCustomer() != null) {
            Customer customer = user.getCustomer();
            if (customer == null) {
                customer = new Customer();
                customer.setStatus(true);
                customer.setDataCreated(LocalDateTime.now());
                user.setCustomer(customer);
            }

            if (request.getCustomer().getFirstName() != null) {
                customer.setFirstName(request.getCustomer().getFirstName());
            }
            if (request.getCustomer().getLastName() != null) {
                customer.setLastName(request.getCustomer().getLastName());
            }
            if (request.getCustomer().getPhone() != null) {
                customer.setPhone(request.getCustomer().getPhone());
            }
            customer.setDataUpdated(LocalDateTime.now());
        }

        user.setDataUpdated(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(savedUser);
    }
}
