package fr.jerome.springbootrestserverapi.service;

import fr.jerome.springbootrestserverapi.dao.UserRepository;
import fr.jerome.springbootrestserverapi.model.User;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    /*
    Annotation permettant d'instancier automatiquement un objet
    en attribut d'une classe à son instanciation (pas besoin de new)
     */
    @Autowired
    private UserRepository userRepository;
    //Les Interfaces DAO peuvent être injectées directement sans avoir à les annoter comme Bean

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Collection<User> getAllUsers() {
        return IteratorUtils.toList(userRepository.findAll().iterator());
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    @Transactional(readOnly = false)
    public User saveOrUpdateUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
