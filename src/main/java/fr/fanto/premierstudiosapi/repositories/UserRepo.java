package fr.fanto.premierstudiosapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.fanto.premierstudiosapi.entities.User;

public interface UserRepo extends JpaRepository<User, String> {
    
}
