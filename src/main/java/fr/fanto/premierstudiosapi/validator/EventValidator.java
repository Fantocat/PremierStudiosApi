package fr.fanto.premierstudiosapi.validator;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventValidator {

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @FutureOrPresent(message = "Date must be in the present or future")
    private LocalDate date;

    @NotNull(message = "Time cannot be null")
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", example = "14:30")
    private LocalTime time;

    @NotNull(message = "Location cannot be null")
    @Size(min = 3, max = 200, message = "Location must be between 3 and 200 characters")
    private String location;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
}

