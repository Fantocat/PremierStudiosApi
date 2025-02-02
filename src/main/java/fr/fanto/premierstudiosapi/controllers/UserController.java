package fr.fanto.premierstudiosapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.fanto.premierstudiosapi.models.ApiResponse;
import fr.fanto.premierstudiosapi.services.UserService;
import fr.fanto.premierstudiosapi.validator.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Endpoints for user management")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account.")
    public ResponseEntity<ApiResponse<String>> register(@Validated UserValidator user) {
        ApiResponse<String> response = userService.doRegister(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    
}
