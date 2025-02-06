package fr.fanto.premierstudiosapi.configs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

import fr.fanto.premierstudiosapi.exceptions.InternalServerErrorException;
import fr.fanto.premierstudiosapi.exceptions.ResourceNotFoundException;
import fr.fanto.premierstudiosapi.exceptions.UserAlreadyExistsException;
import fr.fanto.premierstudiosapi.models.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Validation Error",
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Validation Error",
                    value = "{\n" +
                            "  \"success\": false,\n" +
                            "  \"statusCode\": 400,\n" +
                            "  \"message\": \"Validation Error\",\n" +
                            "  \"data\": null\n" +
                            "}"
                )
            }
        )
    )
    public ResponseEntity<ApiResponse<Object>> handleValidationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ApiResponse<Object> response = new ApiResponse<>(false, 400, "Validation Error", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(AuthenticationException.class)
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "401",
        description = "Unauthorized",
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Unauthorized",
                    value = "{\n" +
                            "  \"success\": false,\n" +
                            "  \"statusCode\": 401,\n" +
                            "  \"message\": \"Unauthorized\",\n" +
                            "  \"data\": null\n" +
                            "}"
                )
            }
        )
    )
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedException(AuthenticationException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, 401, "Unauthorized - Invalid or missing authentication credentials", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "403",
        description = "Access Denied",
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Access Denied",
                    value = "{\n" +
                            "  \"success\": false,\n" +
                            "  \"statusCode\": 403,\n" +
                            "  \"message\": \"Access Denied\",\n" +
                            "  \"data\": null\n" +
                            "}"
                )
            }
        )
    )
    public ResponseEntity<ApiResponse<Object>> handleForbiddenException(AccessDeniedException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, 403, "Forbidden - Insufficient permissions", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Internal Server Error Example",
                    value = "{\n" +
                            "  \"success\": false,\n" +
                            "  \"statusCode\": 500,\n" +
                            "  \"message\": \"Internal Server Error\",\n" +
                            "  \"data\": null\n" +
                            "}"
                )
            }
        )
    )
    public ResponseEntity<ApiResponse<Object>> handleInternalServerErrorException(InternalServerErrorException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, 500, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "409",
        description = "User Already Exists",
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "User Already Exists Example",
                    value = "{\n" +
                            "  \"success\": false,\n" +
                            "  \"statusCode\": 409,\n" +
                            "  \"message\": \"User Already Exists\",\n" +
                            "  \"data\": null\n" +
                            "}"
                )
            }
        )
    )
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, 409, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "404",
        description = "Resource not found",
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Resource Not Found Example",
                    value = "{\n" +
                            "  \"success\": false,\n" +
                            "  \"statusCode\": 404,\n" +
                            "  \"message\": \"Resource not found with ID: 123\",\n" +
                            "  \"data\": null\n" +
                            "}"
                )
            }
        )
    )
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, 404, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
