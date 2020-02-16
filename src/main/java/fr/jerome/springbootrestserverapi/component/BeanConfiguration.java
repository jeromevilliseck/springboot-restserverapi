package fr.jerome.springbootrestserverapi.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Classe permettant de déclarer des beans
 * Spring construira automatiquement des instances de cet objet
 */
/*
Annotation indiquant à Spring que cette classe est une source de configuration des beans
On pourra utiliser un objet BCryptPasswordEncoder n'importe ou grâce à l'annotation @Autowired
 */
@Configuration
public class BeanConfiguration extends WebMvcConfigurerAdapter {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
