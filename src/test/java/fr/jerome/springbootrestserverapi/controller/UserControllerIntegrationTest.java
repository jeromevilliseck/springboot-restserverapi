package fr.jerome.springbootrestserverapi.controller;

import fr.jerome.springbootrestserverapi.model.Role;
import fr.jerome.springbootrestserverapi.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate; //dépendance nécessaire pour écrire les requêtes HTTP.
    private static final String URL = "http://localhost:8484";//url du serveur REST. Cet url peut être celle d'un serveur distant

    private String getURLWithPort(String uri) {
        return URL + uri;
    }

    @Test
    public void testFindAllUsers() throws Exception {
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(getURLWithPort("/user/users"), Object.class);
        assertNotNull(responseEntity);
        @SuppressWarnings("unchecked")
        Collection<User> userCollections = (Collection<User>) responseEntity.getBody();
        assertEquals("Réponse inattendue", HttpStatus.FOUND.value(), responseEntity.getStatusCodeValue());
        assertNotNull(userCollections);
        assertEquals(4, userCollections.size());
        // on a bien 4utulisateurs, 3utilisateurs initialisés par les scripts data.sql
        // a noter que l'utilisateur crée dans la méthode testSaveUser ci dessous est comptabilisé, les tests de ws ne s'execute pas de manière procédurale
    }

    @Test
    public void testSaveUser() throws Exception {
        User user = new User("PIPO", "newPassword", 1);
        ResponseEntity<User> userEntitySaved = restTemplate.postForEntity(getURLWithPort("/user/users"), user, User.class);

        assertNotNull(userEntitySaved); //On verifie que l'utilisateur enregistré à bien été retourné toujours faire des assertNotNull des objets avant de les controler dans des assertions
        //On vérifie que le code de réponse HTTP est celui attendu
        assertEquals("Réponse inattendue", HttpStatus.CREATED.value(), userEntitySaved.getStatusCodeValue());
        //Cela affichera Réponse inattendue que si on obtient pas le bon statut
        User userSaved = userEntitySaved.getBody();
        assertNotNull(userSaved);
        assertEquals(user.getLogin(), userSaved.getLogin());
        assertEquals("ROLE_USER", userSaved.getRoles().iterator().next().getRoleName());
        //On verifie l'utilisateur enregistré à bien un champ ROLE_USER
    }

    @Test
    public void testFindByLoginAndPassword() throws Exception {

        User userTofindByLoginAndPassword = new User("admin@admin.com", "admin", 1);
        ResponseEntity<User> responseEntity = restTemplate.postForEntity(
                getURLWithPort("/user/users"), userTofindByLoginAndPassword, User.class); //enregistrement de l'utlisateur: ws1
        ResponseEntity<User> responseEntity2 = restTemplate.getForEntity(getURLWithPort("/user/users/admin@admin.com"), User.class); //Le deuxième paramètre c'est le type de retour que fourni l'objet ResponseEntity à l'appel du ws
        assertNotNull(responseEntity);
        assertNotNull(responseEntity2);
        User userFound = responseEntity2.getBody();

        assertEquals("Réponse inattendue", HttpStatus.FOUND.value(), responseEntity2.getStatusCodeValue());
        assertNotNull(userFound);
        assertEquals(Long.valueOf(6), userFound.getId());
    }

    @Test
    public void testFindByLoginAndPassword_notFound() throws Exception {
        User userTofindByLoginAndPassword = new User("unknowUser", "password3", 0);
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(
                getURLWithPort("user/users/" + userTofindByLoginAndPassword.getLogin()), User.class);
        assertNotNull(responseEntity);
        assertEquals("Réponse inattendue", HttpStatus.NO_CONTENT.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    public void testUpdateUser() throws Exception {
        ResponseEntity<User> responseEntityToUpdate = restTemplate.postForEntity(
                getURLWithPort("user/users"), new User("login3", "password3", 1), User.class);
        User userFromDBtoUpdate = responseEntityToUpdate.getBody();
        // on met à jour l'utilisateur en lui attribuant le role admin, nouveau login et mot de passe
        userFromDBtoUpdate.setLogin("newLogin");
        userFromDBtoUpdate.setPassword("newPassword");
        userFromDBtoUpdate.setActive(1);
        Role role = new Role("ROLE_ADMIN");
        userFromDBtoUpdate.getRoles().add(role);

        URI uri = UriComponentsBuilder.fromHttpUrl(URL).path("user/users/4").build().toUri();

        HttpEntity<User> requestEntity = new HttpEntity<User>(userFromDBtoUpdate);

        ResponseEntity<User> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, User.class);
        assertNotNull(responseEntity);
        User userUpdated = responseEntity.getBody();
        assertNotNull(userUpdated);
        assertEquals("Réponse inattendue", HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
        assertEquals(userFromDBtoUpdate.getLogin(), userUpdated.getLogin());
    }

    @Test
    public void testDeleteUser() throws Exception {

        URI uri = UriComponentsBuilder.fromHttpUrl(URL).path("user/users/2").build().toUri();

        ResponseEntity<Void> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertNotNull(responseEntity);
        assertEquals("Réponse inattendue", HttpStatus.GONE.value(), responseEntity.getStatusCodeValue());
    }
}