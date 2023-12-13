package com.example.demo.Repository.UserRepo;

import com.example.demo.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select u from User u where u.email = ?1 and u.isDeleted = false")
    Optional<User> findUserByEmail(String email);
    @Query("select u from User u where u.isDeleted = false ")
    List<User> findUsers();
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1 AND u.isDeleted = false")
    List<User> findUsersByRoleName(String roleName);
    @Override
    @Query("select u from User u where u.id = ?1 and u.isDeleted = false ")
    Optional<User> findById(Integer userId);
}
