package com.example.demo.Service;

import com.example.demo.Auth.RegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;


public interface UserService {

    public ResponseEntity<Object> getAllUser() throws JsonProcessingException;
    public ResponseEntity<Object> getUserById(Integer userId) throws JsonProcessingException;
    ResponseEntity<Object> addNewUser(RegisterRequest request);
    public ResponseEntity<Object> deleteUser(Integer userId) ;
    public ResponseEntity<Object> updateUser(Integer userid, String fullname, String email, String password) ;
}
