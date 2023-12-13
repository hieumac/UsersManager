package com.example.demo.Repository.CustomerRepo;

import com.example.demo.Entity.Customer.Customer;
import com.example.demo.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    @Query("select c from Customer c where c.email = ?1 and c.isDeleted = false")
    Optional<Customer> findCustomerByEmail(String email);
    @Query("select c from Customer c where c.isDeleted = false ")
    List<Customer> findCustomers();
    @Override
    @Query("select c from Customer c where c.id = ?1 and c.isDeleted = false ")
    Optional<Customer> findById(Integer customerId);
}
