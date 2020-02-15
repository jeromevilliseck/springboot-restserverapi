package fr.jerome.springbootrestserverapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'application permettant son execution
 * Classe obligatoirement à la racine du package principal pour permettre à Spring de scanner les sous-packages
 */
@SpringBootApplication
/*
Annotation permettant de scanner le package courant et ses sous-packages (Spring 1.2.0 Minimum)
Equivaut à l'ensemble des annotations
- @Configuration (configurer une classe comme une source de définition des beans spring)
- @EnableAutoConfiguration
- @ComponentScan (autorise spring à rechercher tous les composants, les configurations et autres services de
l'application et à initialiser tous les controleurs)

Dispense de l'utilisation de:
@EnableWebMvc (indique qu'il s'agit d'une application SpringMVC)
*/
public class SpringbootRestserverapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootRestserverapiApplication.class, args);
	}

}
