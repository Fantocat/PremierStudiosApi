package fr.fanto.premierstudiosapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.fanto.premierstudiosapi.entities.Event;
import fr.fanto.premierstudiosapi.entities.User;
import fr.fanto.premierstudiosapi.exceptions.InternalServerErrorException;
import fr.fanto.premierstudiosapi.exceptions.ResourceNotFoundException;
import fr.fanto.premierstudiosapi.models.ApiResponse;
import fr.fanto.premierstudiosapi.models.Attendees;
import fr.fanto.premierstudiosapi.repositories.EventRepo;
import fr.fanto.premierstudiosapi.validator.EventValidator;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepo eventRepo;

    public void createEvent(EventValidator eventValidator) {
        try {
            Event event = Event.builder()
                            .name(eventValidator.getName())
                            .date(eventValidator.getDate())
                            .date(eventValidator.getDate())
                            .description(eventValidator.getDescription())
                            .time(eventValidator.getTime())
                            .build();
            eventRepo.save(event);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to create the event due to a database error.");
        }
    }

    public ApiResponse<String> updateEvent(Long id, EventValidator event) {
        Event event2 = eventRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        try {
            event2.setName(event.getName());
            event2.setDate(event.getDate());
            event2.setTime(event.getTime());
            event2.setDescription(event.getDescription());
            event2.setLocation(event.getLocation());
            return new ApiResponse<>(true, 200, "Event updated successfully", null);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to update the event due to a database error.");
        }
    }

    public ApiResponse<String> deleteEvent(Long id) {
        Optional<Event> existingEvent = eventRepo.findById(id);
        if (existingEvent.isEmpty()) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }

        try {
            eventRepo.deleteById(id);
            return new ApiResponse<>(true, 200, "Event deleted successfully", null);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to fetch events due to a database error.");
        }
    }

    public Page<Event> getAllEvents(Pageable pageable) {
        try {
            return eventRepo.findAll(pageable);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to fetch events due to a database error.");
        }
    }

    public Event getEvent(Long id) {
        return eventRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Event not found with id: " + id)
        );
    }

    public ApiResponse<Event> getEventResponse(Long id) {
        try {
            Event event = getEvent(id);
            return new ApiResponse<>(true, 200, "Event fetched successfully", event);
        }  catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }  catch (Exception e) {
            throw new InternalServerErrorException("Failed to fetch events due to a database error.");
        }
    }

    public ApiResponse<Iterable<Attendees>> getAttendees(Long id) {
        try {
            Event event = getEvent(id);

            List<Attendees> attendeesList = event.getUsers().stream()
                .map(user -> new Attendees(user.getEmail(), user.getUsername()))
                .collect(Collectors.toList());

            return new ApiResponse<>(true, 200, "Attendees fetched successfully", attendeesList);
        } catch (Exception e) {
            return new ApiResponse<>(false, 500, "Failed to fetch attendees", null);
        }
    }

    public List<Event> searchEvents(String name, LocalDate date, String location) {
        try {
            return eventRepo.searchEvents(name, date, location);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to search for events due to a database error.");
        }
    }

    public ApiResponse<String> register(Long id, User user){
        Event event = eventRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        event.getUsers().add(user);
        try {
            eventRepo.save(event);
            return new ApiResponse<>(true, 200, "Attendee sucesfully add to event", null);
        } catch (Exception e){
            throw new InternalServerErrorException("Failed to add user to event due to a database error.");
        }
    }
}
