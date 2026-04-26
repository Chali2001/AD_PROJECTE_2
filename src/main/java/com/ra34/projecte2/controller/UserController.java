package com.ra34.projecte2.controller;

import com.ra34.projecte2.dto.UserCreateRequestDTO;
import com.ra34.projecte2.dto.UserAddRolesRequestDTO;
import com.ra34.projecte2.dto.UserResponseDTO;
import com.ra34.projecte2.service.UserService;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Crea user + customer en una única transacción y devuelve solo datos del user.
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreateRequestDTO request) {

        UserResponseDTO response = userService.createUser(request);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    // Consulta un usuario por id y devuelve su información básica con el customer.
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {

        UserResponseDTO response = userService.getUser(id);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    // Consulta todos los usuarios y devuelve id, email y customer.
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> response = userService.getUsers();
        return ResponseEntity.ok(response);
    }

    // Añade una lista de roles a un usuario existente y devuelve el usuario con sus roles.
    @PutMapping("/{id}/roles")
    public ResponseEntity<UserResponseDTO> addRoles(@PathVariable Long id, @RequestBody UserAddRolesRequestDTO request) {

        UserResponseDTO response = userService.addRoles(id, request);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    // Borra una lista de roles del usuario sin eliminar los roles de la base de datos.
    @DeleteMapping("/{id}/roles")
    public ResponseEntity<UserResponseDTO> removeRoles(@PathVariable Long id, @RequestBody UserAddRolesRequestDTO request) {

        UserResponseDTO response = userService.removeRoles(id, request);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    // Modifica datos del user y/o del customer relacionado en una sola transacción.
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserCreateRequestDTO request) {

        UserResponseDTO response = userService.updateUser(id, request);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }
}
