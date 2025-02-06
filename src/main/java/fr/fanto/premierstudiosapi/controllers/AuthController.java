package fr.fanto.premierstudiosapi.controllers;

import fr.fanto.premierstudiosapi.models.ApiResponse;
import fr.fanto.premierstudiosapi.models.LoginRequest;
import fr.fanto.premierstudiosapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Auth Management", description = "Endpoints for auth management")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "User login", 
    description = "Authenticate a user and return a JWT token.")
    public ResponseEntity<ApiResponse<String>> login(LoginRequest login){
        ApiResponse<String> response = userService.dologin(login);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}