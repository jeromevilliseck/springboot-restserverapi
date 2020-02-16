package fr.jerome.springbootrestserverapi.dao;

import fr.jerome.springbootrestserverapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface implémentant toutes les méthodes du CRUD nativement
 * (identique pour toutes les interfaces étendant JpaRepository)
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
