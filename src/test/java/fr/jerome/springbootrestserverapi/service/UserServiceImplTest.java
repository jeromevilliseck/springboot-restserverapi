package fr.jerome.springbootrestserverapi.service;

import fr.jerome.springbootrestserverapi.dao.UserRepository;
import fr.jerome.springbootrestserverapi.model.Role;
import fr.jerome.springbootrestserverapi.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @TestConfiguration //Création des beans nécessaires pour les tests
    /*
    Permet d'obtenir un objet d'instance de service
    N'expose grâce à l'annotation le service que l'ors de la phase de test pour éviter les conflits

     */
    static class UserServiceImplTestContextConfiguration {

        @Bean//bean de service
        public UserService userService(){
            return new UserServiceImpl();
        }

        /*
        @Bean//nécessaire pour hacher le mot de passe sinon échec des tests
        public BCryptPasswordEncoder passwordEncoder() {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            return bCryptPasswordEncoder;
        }
        */
    }

    @Autowired
    private UserService userService;

    @MockBean //création d'un mockBean pour UserRepository
    /*
    Permet de mocker les retour de la BDD fictivement dans le cadre de tests pour la couche DAO avec Mockito
     */
    private UserRepository userRepository;

    User user = new User("Dupont", "password", 1);

    @Test
    public void testFindAllUsers() throws Exception {
        User user = new User("Dupont", "password", 1);
        Role role = new Role("USER_ROLE");//initialisation du role utilisateur
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        List<User> allUsers = Arrays.asList(user);
        Mockito.when(userRepository.findAll()).thenReturn(allUsers); //On mock une réponse de la couche DAO
        Collection<User> users = userService.getAllUsers();
        /*getAllUsers() de la couche service utilise findAll() dela couche dao, ce sera ici grace au mock l'objet allUsers
        qui sera retourne sans impact sur la base de donnees (simulation d'un retour de la base)*/
        assertNotNull(users);
        assertEquals(users, allUsers);
        assertEquals(users.size(), allUsers.size());
        verify(userRepository).findAll();
    }

    @Test
    public void testSaveUser() throws Exception {
        User user = new User("Dupont", "password", 1);
        User userMock = new User(1L,"Dupont", "password", 1);
        Mockito.when(userRepository.save((user))).thenReturn(userMock);
        User userSaved = userService.saveOrUpdateUser(user);
        assertNotNull(userSaved);
        assertEquals(userMock.getId(), userSaved.getId());
        assertEquals(userMock.getLogin(), userSaved.getLogin());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testFindUserByLogin() {
        User user = new User("Dupont", "password", 1);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        User userFromDB = userService.findByLogin(user.getLogin());
        assertNotNull(userFromDB);
        assertThat(userFromDB.getLogin(), is(user.getLogin()));
        verify(userRepository).findByLogin(any(String.class));
    }

    @Test
    public void testDelete() throws Exception {
        User user = new User("Dupont", "password", 1);
        User userMock = new User(1L,"Dupont", "password", 1);
        Mockito.when(userRepository.save((user))).thenReturn(userMock);
        User userSaved = userService.saveOrUpdateUser(user);
        assertNotNull(userSaved);
        assertEquals(userMock.getId(), userSaved.getId());
        userService.deleteUser(userSaved.getId());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User userToUpdate = new User(1L,"Dupont", "password", 1);
        User userUpdated = new User(1L,"Paul", "password", 1);
        Mockito.when(userRepository.save((userToUpdate))).thenReturn(userUpdated);
        User userFromDB = userService.saveOrUpdateUser(userToUpdate);
        assertNotNull(userFromDB);
        assertEquals(userUpdated.getLogin(), userFromDB.getLogin());
        verify(userRepository).save(any(User.class));
    }
}