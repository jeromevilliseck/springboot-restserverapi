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
    Adresse: http://localhost:8080/user/users (GET)
     */
    @GetMapping(value = "/users")
    public ResponseEntity<Collection<User>> getAllUsers(){
        Collection<User> users = userService.getAllUsers();
        logger.info("liste des utilisateurs : " + users.toString());
        return new ResponseEntity<>(users, HttpStatus.FOUND);
    }

    /*
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
