package fr.jerome.springbootrestserverapi.component;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe permettant de déclarer des beans
 * Spring construira automatiquement des instances de cet objet
 * @implNote Suite à problème avec l'encodage de BCrypt avec Spring Security, désactivation
 * de l'encodage des mots de passe des utilisateurs avec h2 (data.sql)
 */
/*
Annotation indiquant à Spring que cette classe est une source de configuration des beans
On pourra utiliser un objet BCryptPasswordEncoder n'importe ou grâce à l'annotation @Autowired
 */
@Configuration
public class BeanConfiguration implements WebMvcConfigurer {
    /*@Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }*/
}
