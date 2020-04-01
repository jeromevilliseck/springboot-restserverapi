package fr.jerome.springbootrestserverapi.dao;

import fr.jerome.springbootrestserverapi.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)//permet d'établir une liaison entre JUnit et Spring
@DataJpaTest
/*
implémentation Spring de JPA qui fournit une configuration intégrée de la base de données H2, Hibernate, SpringData, et la DataSource.
active également la détection des entités annotées par Entity, et intègre aussi la gestion des logs SQL.
 */
/**
 * Tests servant à controler le bon fonctionnement de la couche la plus basse : la DAO
 */
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    User user = new User("Dupont", "password", 1);

    //ETAPE1 : enregistrer un utilisateur dans le setup
    @Before
    public void setup(){
        entityManager.persist(user);//on sauvegarde l'objet user au début de chaque test
        entityManager.flush();
    }

    //ETAPE2 : faire les test unitaires
    @Test// on a bien 3 utilisateurs initialisés par les scripts data.sql + un nouvel
		// utilisateur crée dans testSaveUser
    public void testFindAllUsers() {
        List<User> users = userRepository.findAll();
        assertThat(4, is(users.size()));//on a trois users dans le fichier d'initialisation data.sql et un utilisateur ajouté lors du setup du test
    }

    @Test
    public void testSaveUser(){
        User user = new User("Paul", "password", 1);
        User userSaved =  userRepository.save(user);
        assertNotNull(userSaved.getId());
        assertThat("Paul", is(userSaved.getLogin()));
    }

    @Test
    public void testFindByLogin() {
        User userFromDB = userRepository.findByLogin("user1");
        assertThat("user1", is(userFromDB.getLogin()));//user2 a été créé lors de l'initialisation du fichier data.sql
    }

    @Test
    public void testDeleteUser(){
        userRepository.delete(user);
        User userFromDB = userRepository.findByLogin(user.getLogin());
        assertNull(userFromDB);
    }

    @Test
    public void testUpdateUser() {//Test si le compte utilisateur est désactivé
        User userToUpdate = userRepository.findByLogin(user.getLogin());
        userToUpdate.setActive(0);
        userRepository.save(userToUpdate);
        User userUpdatedFromDB = userRepository.findByLogin(userToUpdate.getLogin());
        assertNotNull(userUpdatedFromDB);
        assertThat(0, is(userUpdatedFromDB.getActive()));
    }
}