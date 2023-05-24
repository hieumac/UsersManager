package com.example.demo.Service;

import com.example.demo.Auth.AuthenticationRequest;
import com.example.demo.Auth.AuthenticationResponse;
import com.example.demo.Auth.RegisterRequest;
import com.example.demo.Auth.ResponseHandler;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repository.RoleCustomRepository;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleCustomRepository roleCustomRepository;



    public ResponseEntity<Object> login(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }catch (BadCredentialsException e){
            return ResponseHandler.responseBuilder("Email hoặc Password không chính xác",HttpStatus.UNAUTHORIZED,"");
        }

        User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow();
        List<Role> role = null;
        if(user!=null){
           role = roleCustomRepository.getRole(user);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Set<Role> set = new HashSet<>(role);
        user.setRoles(set);
        set.stream().map(Role::getName).map(SimpleGrantedAuthority::new).forEach(authorities::add);
        var jwtToken = jwtService.generateToken(user,authorities);
        return ResponseHandler.responseBuilder("Đăng nhập thành công",HttpStatus.ACCEPTED,jwtToken);
    }
}
