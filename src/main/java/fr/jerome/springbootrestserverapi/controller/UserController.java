package fr.jerome.springbootrestserverapi.controller;

import fr.jerome.springbootrestserverapi.model.Role;
import fr.jerome.springbootrestserverapi.model.User;
import fr.jerome.springbootrestserverapi.service.RoleService;
import fr.jerome.springbootrestserverapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
/*
Annotation @CrossOrigin
permet de favoriser une communication distante entre le client et le serveur, quand le serveur et le client sont
déployés sur deux serveurs distincts, permet d'éviter les problèmes de réseau
 */
@RequestMapping("/user/*")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /*
    Service d'extraction de tous les utilisateurs dans UserController

    Adresse: http://localhost:8080/user/users (GET)
     */
    @GetMapping(value = "/users")
    public ResponseEntity<Collection<User>> getAllUsers(){
        Collection<User> users = userService.getAllUsers();
        logger.info("liste des utilisateurs : " + users.toString());
        return new ResponseEntity<>(users, HttpStatus.FOUND);
    }

    /*
    Service de création d'un utilisateur à ajouter dans la classe UserController

    Adresse: http://localhost:8080/user/users (POST)

    Exemple d'XML d'insertion en POST

    <user>
	<login>user4</login>
	<password>user4</password>
	<active>1</active>
    </user>
     */
    @PostMapping(value = "/users")
    @Transactional
    public ResponseEntity<User> saveUser(@RequestBody User user){
        Set<Role> roles = new HashSet<>();
        Role roleUser = new Role("ROLE_USER"); //Initialisation du role ROLE_USER
        roles.add(roleUser);
        user.setRoles(roles);
        user.setActive(0);

        Set<Role> roleFromDB = extractRole_Java8(user.getRoles(), roleService.getAllRolesStream());
        user.getRoles().removeAll(user.getRoles());
        user.setRoles(roleFromDB);
        User userSave = userService.saveOrUpdateUser(user);
        logger.info("userSave: " + userSave.toString());

        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }

    /*
    Service de recherche d'un utilisateur par son login

    Adresse: http://localhost:8080/user/users/{login_à_rechercher} (GET)
     */
    @GetMapping(value = "/users/{loginName}")
    public ResponseEntity<User> findUserByLogin(@PathVariable("loginName") String login){
        User user = userService.findByLogin(login);
        logger.debug("Utilisateur trouvé: " + user);

        return new ResponseEntity<User>(user, HttpStatus.FOUND);
    }

    /*
    Service de modification d'un utilisateur

    Adresse: http://localhost:8080/user/users/{id_utilisateur_a_update} (PUT)

    Exemple XML de modification du user3

    <user>
	<login>user3</login>
	<password>user3</password>
	<active>1</active>
    </user>
     */
    @PutMapping(value = "/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long id, @RequestBody User user){
        User userToUpdate = userService.getUserById(id);
        if(userToUpdate == null){
            logger.debug("L'utilisateur avec l'identifiant" + id + "n'existe pas");

            return new ResponseEntity<User>(user, HttpStatus.NOT_FOUND);
        }
        logger.info("UPDATE ROLE: " + userToUpdate.getRoles().toString());
        userToUpdate.setLogin(user.getLogin());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setActive(user.getActive());
        User userUpdated = userService.saveOrUpdateUser(userToUpdate);

        return new ResponseEntity<User>(userUpdated, HttpStatus.OK);
    }

    /*
    Service de suppression d'un utilisateur

    Adresse: http://localhost:8080/user/users/{id_utilisateur_a_supprimer} (DELETE)
     */
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") Long id){
        userService.deleteUser(id);

        return new ResponseEntity<Void>(HttpStatus.GONE);
    }

    //Extraction

    //Advanced method which use filter to get User Roles
    private Set<Role> extractRole_Java8(Set<Role> rolesSetFromUser, Stream<Role> roleStreamFromDB){
        //Collect UI role names
        Set<String> uiRoleNames = rolesSetFromUser.stream().map(Role::getRoleName).collect(Collectors.toCollection(HashSet::new));
        //Filter DB roles
        return roleStreamFromDB.filter(role -> uiRoleNames.contains(role.getRoleName())).collect(Collectors.toSet());
    }

    //Advanced method which use filter to get User Roles with compareTo()
    private Set<Role> extractRoleUsingCompareTo_Java8(Set<Role> rolesSetFromUser, Stream<Role> roleStreamFromDB){
        return roleStreamFromDB.filter(roleFromDB -> rolesSetFromUser.stream().anyMatch(roleFromUser -> roleFromUser.compareTo(roleFromDB) == 0)).collect(Collectors.toCollection(HashSet::new));
    }

    //Classical method to navigate on collection
    private Set<Role> extractRole_BeforeJava8(Set<Role> rolesSetFromUser, Collection<Role> rolesFromDB){
        Set<Role> rolesToAdd = new HashSet<>();
        for(Role roleFromUser:rolesSetFromUser){
            for(Role roleFromDB:rolesFromDB){
                if(roleFromDB.compareTo(roleFromUser) == 0){
                    rolesToAdd.add(roleFromDB);
                    break;
                }
            }
        }
        return rolesToAdd;
    }
}
