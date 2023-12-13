package com.example.demo.Entity.Customer;

import com.example.demo.Entity.Customer.Customer;
import com.example.demo.Redis.RedisServiceImp;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomerListener {
    private  final RedisServiceImp redisServiceImp;
    @PrePersist
    public void prePersist(Customer customer){

    }
    @PostPersist
    public void postPersist(Customer customer) {
        redisServiceImp.deleteKey("customers");
    }
    @PreUpdate
    public void preUpdate(Customer customer){}
    @PostUpdate
    public void postUpdate(Customer customer){
        redisServiceImp.deleteKey("customers");
    }
}

