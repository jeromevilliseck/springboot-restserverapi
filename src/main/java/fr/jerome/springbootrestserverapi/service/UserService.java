package fr.jerome.springbootrestserverapi.service;

import fr.jerome.springbootrestserverapi.exception.BusinessResourceException;
import fr.jerome.springbootrestserverapi.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User getUserById(Long id) throws BusinessResourceException; //Ajout de la gestion des exceptions

    User findByLogin(String login);

    User saveOrUpdateUser(User user);

    void deleteUser(Long id);
}
