package com.example.fitness.service;

import com.example.fitness.Repository.UserRepository;
import com.example.fitness.dto.LoginRequest;
import com.example.fitness.dto.RegisterRequest;
import com.example.fitness.dto.UserResponse;
import com.example.fitness.model.User;
import com.example.fitness.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterRequest registerRequest) {
        UserRole role = registerRequest.getRole()!=null? registerRequest.getRole()
                : UserRole.USER;
        User user = User.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();
//       User user = new User(
//               null,
//               registerRequest.getEmail(),
//               registerRequest.getPassword(),
//               registerRequest.getFirstName(),
//               registerRequest.getLastName(),
//               Instant.parse("2011-10-05T14:48:00Z").atZone(ZoneOffset.UTC)
//                       .toLocalDateTime(),
//               Instant.parse("2011-10-05T14:48:00Z").atZone(ZoneOffset.UTC)
//                       .toLocalDateTime(),
//               List.of(),
//               List.of()
//       );
        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }
    public UserResponse mapToResponse(User savedUser) {
        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setPassword(savedUser.getPassword());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setUpdatedAt(savedUser.getUpdatedAt());
        return response;
    }

    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user==null){
            throw new RuntimeException("Invalid Crediatials");
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new RuntimeException("Invalid Crediatials");
        }
        return user;
    }
}
