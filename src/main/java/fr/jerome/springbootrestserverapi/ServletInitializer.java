package fr.jerome.springbootrestserverapi;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Permet l'initialisation de l'application, remplace l'ancien fichier web.xml
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringbootRestserverapiApplication.class);
	}

}
