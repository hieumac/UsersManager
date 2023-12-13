package com.example.demo.Entity.User;

import com.example.demo.Entity.User.User;
import com.example.demo.Redis.RedisServiceImp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UserListener {
    private  final RedisServiceImp redisServiceImp;
    @PrePersist
    public void prePersist(User user){

    }
    @PostPersist
    public void postPersist(User user) {
        redisServiceImp.clear();
    }
    @PreUpdate
    public void preUpdate(User user){}
    @PostUpdate
    public void postUpdate(User user){
        redisServiceImp.deleteKey("users");
    }
}
