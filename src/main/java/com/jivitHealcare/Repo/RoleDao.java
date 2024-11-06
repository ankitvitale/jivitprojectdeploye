package com.jivitHealcare.Repo;

import com.jivitHealcare.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDao extends JpaRepository<Role, String> {
    Optional<Role> findByRoleName(String roleName);

}
