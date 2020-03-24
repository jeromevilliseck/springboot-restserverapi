package fr.jerome.springbootrestserverapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe permettant juste de démarrer et tester l'application, à mettre obligatoirement
 * dans le même package que le classe SpringbootRestserverapiApplication
 */

@Controller
/*
Annotation permettant à Spring d'enregistrer la classe comme un contrôleur, et de mémoriser les
requêtes que cette classe est capable de gérer
 */
public class DefaultController {
    /*
    Integration d'un logger sans configuration grâce à slf4j
    On obtient un logger en passant en paramètre à sa création la classe courante
     */
    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @GetMapping(value = "/")
    /*
    Nouvelle annotation introduite par spring qui remplace l'annotation classique
    @RequestMapping et qui correspond exactement à:
    @RequestMapping(method=RequestMethod.GET, value="/")
    Adresse de test de la configuration: http://localhost:8080

    La classe ResponseEntity permet la gestion des statuts HTTP de réponses (fait le rôle de: ResponseBody + HttpStatus)

    Adresse: http://localhost:8080/
     */
    public ResponseEntity<String> pong(){
        logger.info("Démarrage des services OK");
        return new ResponseEntity<String>("Réponse du serveur:" + HttpStatus.OK.name(), HttpStatus.OK);
    }
}
