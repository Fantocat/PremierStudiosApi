package fr.fanto.premierstudiosapi.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import fr.fanto.premierstudiosapi.entities.Event;
import fr.fanto.premierstudiosapi.entities.User;
import fr.fanto.premierstudiosapi.models.ApiResponse;
import fr.fanto.premierstudiosapi.models.Attendees;
import fr.fanto.premierstudiosapi.services.EventService;
import fr.fanto.premierstudiosapi.validator.EventValidator;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Event API", description = "API for managing events")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<ApiResponse<String>> createEvent(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Event to create")
            @Validated 
            @org.springframework.web.bind.annotation.RequestBody 
            EventValidator event) {
        eventService.createEvent(event);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Event created successfully", null));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Update an event", description = "Updates an event by its ID")
    public ResponseEntity<ApiResponse<String>> updateEvent(
            @PathVariable Long id, 
            @RequestBody Event event) {
        ApiResponse<String> response = eventService.updateEvent(id, event);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Delete an event", description = "Deletes an event by its ID")
    public ResponseEntity<ApiResponse<String>> deleteEvent(@PathVariable Long id) {
        ApiResponse<String> response = eventService.deleteEvent(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all events", description = "Retrieves a paginated list of events")
    public ResponseEntity<ApiResponse<Page<Event>>> getEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Event> events = eventService.getAllEvents(pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Events fetched successfully", events));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<ApiResponse<Event>> getEvent(@PathVariable Long id) {
        ApiResponse<Event> response = eventService.getEventResponse(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}/attendees")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Get attendees of an event", description = "Retrieves attendees for a specific event")
    public ResponseEntity<ApiResponse<Iterable<Attendees>>> getAttendees(@PathVariable Long id) {
        ApiResponse<Iterable<Attendees>> reponse = eventService.getAttendees(id);
        return ResponseEntity.status(reponse.getStatusCode()).body(reponse);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for events", description = "Search for events by name, date, or location")
    public ResponseEntity<ApiResponse<Iterable<Event>>> getEventBySearch(
            @RequestParam(required = false) 
            @Size(min = 3, message = "Name must be at least 3 characters") 
            String name,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) 
            @FutureOrPresent(message = "Date must be in the present or future")
            LocalDate date,
            @RequestParam(required = false) String location) {
        
        List<Event> events = eventService.searchEvents(name, date, location);
        if (events.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(true, 200, "No events found matching the criteria", events));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, 200, "Events fetched successfully", events));
    }

    @PostMapping("/{id}/register")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Register authenticated user for an event", description = "Registers a user for a specific event")
    public ResponseEntity<ApiResponse<String>> register(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
        ApiResponse<String> response = eventService.register(id, user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
