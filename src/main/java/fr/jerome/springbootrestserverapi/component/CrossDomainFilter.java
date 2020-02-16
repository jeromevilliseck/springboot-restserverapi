package fr.jerome.springbootrestserverapi.component;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Classe permettant de palier aux problèmes de Cross-Domain
 * ce sont des problèmes réseaux qui peuvent entraver la communication
 * Indique au serveur quel type d'entêtes HTTP à prendre en considération
 */
/*
Annotation permettant à Spring de considérer la classe comme un composant
et pourra utiliser le filtre sans problème
 */
@Component
public class CrossDomainFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException{
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "origin, contenttype, accept, x-req");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
