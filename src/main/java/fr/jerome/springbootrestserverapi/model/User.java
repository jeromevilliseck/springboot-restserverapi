package fr.jerome.springbootrestserverapi.model;

import fr.jerome.springbootrestserverapi.dto.UserDTO;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Un utilisateur avec login et password, actif ou non actif
 */
@Entity
@Table(name = "UTILISATEUR")
/*
Il ne faut pas donner le nom USER à des tables car elle existent déjà en base de donnée
 */
@XmlRootElement(name = "user")
/*
Annotation permettant de construire un objet XML lors des tests de communications entre le client et le serveur
 */
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", updatable = false, nullable = false)
    private Long id;

    @Column(name = "LOGIN", unique = true, insertable = true, updatable = true, nullable = false)
    private String login;

    @Column(name = "USER_PASSWORD", insertable = true, updatable = true, nullable = false)
    private String password;

    @Column(name = "USER_ACTIVE", insertable = true, updatable = true, nullable = false)
    private Integer active;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private Set<Role> roles = new HashSet<>();

    public User(){
        super();
    }

    public User(String login, String password, Integer active){
        this.login = login;
        this.password = password;
        this.active = active;
    }

    public User(Long id, String login){
        this.id = id;
        this.login = login;
    }

    public User(String login){
        this.login = login;
    }

    public User(UserDTO userDTO){
        this.setId(userDTO.getId());
        this.setLogin(userDTO.getLogin());
        this.setPassword(userDTO.getPassword());
    }

    /*
    public User(UserRegistrationForm userRegistrationForm){
        this.setLogin(userRegistrationForm.getLogin());
        this.setPassword(userRegistrationForm.getPassword());
    }
     */

    public User(Long id, String login, String password, Integer active){
        this.id = id;
        this.login = login;
        this.password = password;
        this.active = active;
    }

    public Long getId(){
        return id;
    }

    @XmlElement
    public void setId(Long id){
        this.id = id;
    }

    public String getLogin(){
        return login;
    }

    @XmlElement
    public void setLogin(String login){
        this.login = login;
    }

    public String getPassword(){
        return password;
    }

    @XmlElement
    public void setPassword(String password){
        this.password = password;
    }

    public Integer getActive(){
        return active;
    }

    @XmlElement
    public void setActive(Integer active){
        this.active = active;
    }

    public Set<Role> getRoles(){
        return roles;
    }

    @XmlElement
    public void setRoles(Set<Role> roles){
        this.roles = roles;
    }

    @Override
    public String toString(){
        return "User [id=" + this.id + ", login=" + this.login + ", pass=XXXX-XXX, active=" + this.active + ", roles=" + this.roles + "]";
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((roles == null) ? 0 : roles.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(active, user.active) &&
                Objects.equals(roles, user.roles);
    }
}
