package com.example.demo.Controller;

import com.example.demo.Service.*;
import com.example.demo.Auth.*;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.IMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;
    private final UserServiceImp userServiceImp;
    @PostMapping("/register")

    public ResponseEntity<Object> register(
            @RequestBody RegisterRequest request
    ){
    return userServiceImp.addNewUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthenticationRequest request){
        return authService.login(request);
    }
}
