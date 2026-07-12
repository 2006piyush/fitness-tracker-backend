package com.example.fitness.controller;

import com.example.fitness.dto.LoginRequest;
import com.example.fitness.dto.LoginResponse;
import com.example.fitness.dto.RegisterRequest;
import com.example.fitness.dto.UserResponse;
import com.example.fitness.model.User;
import com.example.fitness.security.JwtUtils;
import com.example.fitness.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest){

        return ResponseEntity.ok(userService.register(registerRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        Authentication authentication ;
        try {
            User user = userService.authenticate(loginRequest);
            String token = jwtUtils.genrateToken(user.getId(),user.getRole().name());
            return ResponseEntity.ok(new LoginResponse(
                    token,userService.mapToResponse(user)
            ));
        }catch (AuthenticationException e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

    }
}
