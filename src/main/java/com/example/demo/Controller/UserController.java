package com.example.demo.Controller;

import com.example.demo.Auth.RegisterRequest;
import com.example.demo.Entity.User.User;
import com.example.demo.Firebase.PushNotificationRequest;
import com.example.demo.Service.AuthenticationService;
import com.example.demo.Firebase.FirebaseMessageService;
import com.example.demo.Service.UserServiceImp;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private AuthenticationService authService;

    @GetMapping("/listUsers")
    public ResponseEntity<Object> getAllUser() throws JsonProcessingException {
        return userServiceImp.getAllUser();
    }
    @PostMapping("/addNewUser")
    public ResponseEntity<Object> addNewUser(@RequestBody RegisterRequest request){
        return userServiceImp.addNewUser(request);
    }
    @GetMapping("/idUpdate/{userId}")
    public ResponseEntity<Object> getUserByIdUpdate(
            @PathVariable("userId") Integer userId) throws JsonProcessingException {
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
