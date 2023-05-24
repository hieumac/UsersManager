package com.example.demo.Entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "fullname",length = 45,nullable = false)
    private String fullname;
    @Column(name = "email",length = 45,nullable = false)
    private String email;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "isDeleted",nullable = false)
    private boolean isDeleted;
    @Column(name = "isActive",nullable = false)
    private boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_role",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(Integer id, String fullname, String email, String password, Set<Role> roles) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.isDeleted=false;
        this.isActive=true;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullname='" + fullname + '\'' +
                ", username='" + email + '\'' +
                '}';
    }
}
