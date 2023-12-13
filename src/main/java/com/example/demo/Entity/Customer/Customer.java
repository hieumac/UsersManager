package com.example.demo.Entity.Customer;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@Table(name = "customers")
@EntityListeners(CustomerListener.class)
public class Customer {
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

    public Customer(Integer id, String fullname, String email, String password) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.isDeleted=false;
        this.isActive=true;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

}
