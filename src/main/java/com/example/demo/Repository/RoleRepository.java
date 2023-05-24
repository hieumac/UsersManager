package com.example.demo.Repository;

import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
@Query("select r from Role r where r.name = ?1")
    Role findRoleByName(String rolename);
}
