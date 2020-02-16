package fr.jerome.springbootrestserverapi.dao;

import fr.jerome.springbootrestserverapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);

    @Query("select role from Role role")
    Stream<Role> getAllRolesStream();
}
