package fr.jerome.springbootrestserverapi.service;

import fr.jerome.springbootrestserverapi.dao.RoleRepository;
import fr.jerome.springbootrestserverapi.dao.UserRepository;
import fr.jerome.springbootrestserverapi.exception.BusinessResourceException;
import fr.jerome.springbootrestserverapi.exception.UserNotFoundException;
import fr.jerome.springbootrestserverapi.model.User;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/*
L'annotation @Service est ici optionnelle ici car il n'existe qu'une seule implémentation
Cette annotation permet de déclarer cette classe comme un bean de service
En cas d'implémentation multiples l'annotation @Service est obligatoire
 */

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        super();
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Collection<User> getAllUsers() {
        return IteratorUtils.toList(userRepository.findAll().iterator());
    }

    @Override
    public User getUserById(Long id) throws BusinessResourceException{ //Ajout des exceptions
        return userRepository.getOne(id);
    }

    @Override
    public User findByLogin(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null){
            throw new BusinessResourceException("User Not Found", "L'utilisateur avec ce login n'existe pas :" + login, HttpStatus.NO_CONTENT);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = false)
    public User saveOrUpdateUser(User user) {
        try {
            user.setPassword(user.getPassword());
            return userRepository.save(user);
        } catch (Exception ex) {
            throw new BusinessResourceException("Create Or Update User Error", "Erreur de création ou de mise à jour de l'utilisateur: "+user.getLogin(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUser(Long id) {
        try{
            userRepository.deleteById(id);
        }catch(Exception ex){
            throw new BusinessResourceException("Delete User Error", "Erreur de suppression de l'utilisateur avec l'identifiant: "+id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
