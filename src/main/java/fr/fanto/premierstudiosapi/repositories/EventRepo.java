package fr.fanto.premierstudiosapi.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.fanto.premierstudiosapi.entities.Event;

public interface EventRepo extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE " +
       "(:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
       "(:date IS NULL OR e.date = :date) AND " +
       "(:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<Event> searchEvents(@Param("name") String name, 
                             @Param("date") LocalDate date, 
                             @Param("location") String location);
}