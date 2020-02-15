package fr.jerome.springbootrestserverapi.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

@Entity
/*
Annotation indiquant que la classe sera une table de la base de données
Permet de ne plus avoir besoin du fichier persistence.xml
 */
@Table(name = "ROLE")
/*
Annotation permettant de donner le nom utilisateur à la table
Permet de ne plus avoir besoin du fichier persistence.xml
 */
@XmlRootElement(name = "role")
public class Role implements Serializable {
    private static final long serialVersionUID = 2284252532274015507L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID", updatable = false, nullable = false)
    private int id;

    @Column(name = "ROLE_NAME", updatable = true, nullable = false)
    private String roleName;

    public Role(){
        super();
    }

    public Role(String roleName){
        super();
        this.roleName = roleName;
    }

    public int getId(){
        return id;
    }

    @XmlElement
    public void setId(int id){
        this.id = id;
    }

    public String getRoleName(){
        return roleName;
    }

    @XmlElement
    public void setRoleName(String roleName){
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id &&
                Objects.equals(roleName, role.roleName);
    }

    public int compareTo(Role role){
        return this.roleName.compareTo(role.getRoleName());
    }
}
