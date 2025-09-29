package app.doctor_connect_backend.auth.app;

import app.doctor_connect_backend.auth.web.DTOs.UserResponse;
import app.doctor_connect_backend.user.Roles;
import app.doctor_connect_backend.user.User;
import app.doctor_connect_backend.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder encoder;

    public AuthService(PasswordEncoder encoder, UserService userService) {
        this.userService = userService;
        this.encoder = encoder;
    }
    public UserResponse login(String email, String password) {
       email = email.trim().toLowerCase();
        try {
            var user = userService.findEmail(email);
            if (encoder.matches(password, user.getPasswordHash())) {
                return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getUserRole(), user.getCreatedAt());
            } else {
                throw new RuntimeException("Invalid password");
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    public UserResponse register(String fullName, String email, String password, Roles role){
        email = email.trim().toLowerCase();
        try{
            String hash = encoder.encode(password);
            var saved = userService.createUser(email, hash, fullName, role);
            return new UserResponse(saved.getId(), saved.getFullName(), saved.getEmail(), saved.getUserRole(), saved.getCreatedAt( ));
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }


}
