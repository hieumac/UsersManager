package com.example.demo.Service;

import com.example.demo.Auth.RegisterRequest;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import org.springframework.http.ResponseEntity;


public interface UserService {

    public ResponseEntity<Object> getAllUser();
    public ResponseEntity<Object> getUserById(Integer userId);
    ResponseEntity<Object> addNewUser(RegisterRequest request);
    public ResponseEntity<Object> deleteUser(Integer userId) ;
    public ResponseEntity<Object> updateUser(Integer userid, String fullname, String email, String password) ;
}
