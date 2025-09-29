package app.doctor_connect_backend.auth.web;

import app.doctor_connect_backend.auth.app.AuthService;
import app.doctor_connect_backend.auth.web.DTOs.LoginRequest;
import app.doctor_connect_backend.auth.web.DTOs.RegisterRequest;
import app.doctor_connect_backend.auth.web.DTOs.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5174")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping ("/login")
    public ResponseEntity<UserResponse> login (@RequestBody LoginRequest loginRequest){
        UserResponse response = authService.login(loginRequest.email(), loginRequest.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest){
        UserResponse response = authService.register(registerRequest.fullName(), registerRequest.email(), registerRequest.password(), registerRequest.role());
        return ResponseEntity.ok(response);
    }
}
