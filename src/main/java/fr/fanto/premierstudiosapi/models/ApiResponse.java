package fr.fanto.premierstudiosapi.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

     @Schema(description = "Indicates whether the API call was successful", example = "true")
    private boolean success;

    @Schema(description = "HTTP status code of the response", example = "200")
    private int statusCode;

    @Schema(description = "A message describing the result of the API call", example = "Operation successful")
    private String message;

    @Schema(description = "The data payload of the response. This can vary depending on the API endpoint.")
    private T data; 
}
