package ru.spbstu.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.spbstu.repository.UserRepository;
import ru.spbstu.security.jwt.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody AuthRequest request) {
        try {
            String name = request.getUserName();
            String token = jwtTokenProvider.createToken(name,
                    userRepository.findUserByUserName(name).orElseThrow(() -> new UsernameNotFoundException("User not found")).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("userName", name);
            model.put("token", token);

            return ResponseEntity.ok(model);
        } catch (AuthenticationException exeption) {
            throw new BadCredentialsException("Invalid username or password " + request.getUserName());
        }
    }
}