package dev.mateorossello.entertainmentlists.controllers;

import dev.mateorossello.entertainmentlists.configuration.JwtUtil;
import dev.mateorossello.entertainmentlists.dtos.AuthRequest;
import dev.mateorossello.entertainmentlists.dtos.AuthResponse;
import dev.mateorossello.entertainmentlists.dtos.SuccessResponse;
import dev.mateorossello.entertainmentlists.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse register(@Valid @RequestBody AuthRequest request) {
        userService.registerUser(request.username(), request.password());
        return new SuccessResponse("User registered successfully");
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        
        final UserDetails userDetails = userService.loadUserByUsername(request.username());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new AuthResponse(jwt);
    }
}
