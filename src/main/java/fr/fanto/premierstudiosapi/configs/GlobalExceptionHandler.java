package fr.fanto.premierstudiosapi.configs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fr.fanto.premierstudiosapi.exceptions.InternalServerErrorException;
import fr.fanto.premierstudiosapi.exceptions.ResourceNotFoundException;
import fr.fanto.premierstudiosapi.exceptions.UserAlreadyExistsException;
import fr.fanto.premierstudiosapi.models.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestControllerAdvice
public class GlobalExceptionHandler {

    

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
    public ResponseEntity<ApiResponse<Object>> handleUserAlreadyExistsException(InternalServerErrorException ex) {
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Validation error: The request contains invalid or missing fields",
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Validation Error Example",
                    value = "{\n" +
                            "  \"success\": false,\n" +
                            "  \"statusCode\": 400,\n" +
                            "  \"message\": \"Validation failed: Some fields are incorrect\",\n" +
                            "  \"errors\": {\n" +
                            "    \"email\": \"Email is required\",\n" +
                            "    \"password\": \"Password must be at least 8 characters long\"\n" +
                            "  }\n" +
                            "}"
                )
            }
        )
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Erreur de validation des données d'entrée")
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return errors;
    }
}
