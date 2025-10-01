package app.doctor_connect_backend.auth.web;

import app.doctor_connect_backend.auth.app.AuthService;
import app.doctor_connect_backend.auth.web.DTOs.LoginRequest;
import app.doctor_connect_backend.auth.web.DTOs.RegisterRequest;
import app.doctor_connect_backend.auth.web.DTOs.UserResponse;
import app.doctor_connect_backend.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5174", allowedHeaders = "true")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }
    @PostMapping ("/login")
    public ResponseEntity<UserResponse> login (@RequestBody LoginRequest loginRequest, HttpServletRequest http){
        UserResponse response = authService.login(loginRequest.email(), loginRequest.password());
        http.getSession(true).setAttribute("uid", response.id());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest){
        UserResponse response = authService.register(registerRequest.fullName(), registerRequest.email(), registerRequest.password(), registerRequest.role());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public UserResponse me(HttpServletRequest http){
        var session = http.getSession(false);
        if(session == null){
            System.out.println("User is a guest!");
            return null;
        }
        var uid = (UUID) session.getAttribute("uid");
        if (uid == null) return null;
        var user = userService.findById(uid);                       // load from DB
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getUserRole(), user.getCreatedAt());
    }
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest http){
        var s = http.getSession(false);
        if (s != null) s.invalidate();
        return ResponseEntity.ok().build();
    }

}
