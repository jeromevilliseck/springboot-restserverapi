package fr.jerome.springbootrestserverapi.service;

import fr.jerome.springbootrestserverapi.SpringbootRestserverapiApplication;
import fr.jerome.springbootrestserverapi.dao.RoleRepository;
import fr.jerome.springbootrestserverapi.dao.UserRepository;
import fr.jerome.springbootrestserverapi.model.Role;
import fr.jerome.springbootrestserverapi.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

//@RunWith(SpringRunner.class) //pas besoin car on a fait l'autowired par constructeur sur UserServiceImpl
@SpringBootTest(classes = UserServiceImpl.class)
public class UserServiceImplTest {

    private UserService userService;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Before
    public void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        roleRepository = Mockito.mock(RoleRepository.class);
        userService = new UserServiceImpl(userRepository, roleRepository);
    }

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