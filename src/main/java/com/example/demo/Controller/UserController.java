package com.example.demo.Controller;

import com.example.demo.Auth.RegisterRequest;
import com.example.demo.Auth.ResponseHandler;
import com.example.demo.Entity.User;
import com.example.demo.Service.AuthenticationService;
import com.example.demo.Service.UserServiceImp;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserServiceImp userServiceImp;
    private final AuthenticationService authService;

    @Autowired
    public UserController(UserServiceImp userServiceImp, AuthenticationService authService) {
        this.userServiceImp = userServiceImp;
        this.authService = authService;
    }
    @GetMapping("/listUsers")
    public ResponseEntity<Object> getAllUser(){
        return userServiceImp.getAllUser();
    }
    @PostMapping("/addNewUser")
    public ResponseEntity<Object> addNewUser(@RequestBody RegisterRequest request){
        return userServiceImp.addNewUser(request);
    }
    @GetMapping("/idUpdate/{userId}")
    public ResponseEntity<Object> getUserByIdUpdate(
            @PathVariable("userId") Integer userId){
       return userServiceImp.getUserById(userId);
    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable("userId")Integer userId,@RequestBody User user){
        return userServiceImp.updateUser(userId,user.getFullname(),user.getEmail(),user.getPassword());
    }

    @DeleteMapping ("/delete/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") Integer userId){
        return userServiceImp.deleteUser(userId);
    }
}
