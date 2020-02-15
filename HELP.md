# Read Me First
The following was discovered as part of building this project:

* The original package name 'fr.jerome.springboot-restserverapi' is invalid and this project uses 'fr.jerome.springbootrestserverapi' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Mise en place de l'architecture, amorçage du projet

* [https://start.spring.io](https://start.spring.io)
* Dépendances: Web, JPA, H2
* Packaging: war
* Java: 8

### Explication du rôle des dossiers

* src/main/resources/static: stocker tous les fichiers css, images, ne fournissant pas un contenu dynamique
* src/main/resources/templates: stocker des fichiers web si on utilise les framework Thymeleaf de spring
* src/main/resources/application.properties: pour configurer le projet

### Objectif du projet
#### Creation d'un service complet de gestion d'un utilisateur

* Extraction de tous les utilisateurs
* Extraction d'un utilisateur à base de son identifiant
* Création d'un utilisateur
* Mise à jour d'un utilisateur
* Suppression d'un utilisateur