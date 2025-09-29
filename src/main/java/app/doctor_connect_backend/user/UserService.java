package app.doctor_connect_backend.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(String email, String passwordHash, String fullName, Roles role) {

        String normEmail = email == null ? "" : email.trim().toLowerCase();
        String normName  = fullName == null ? "" : fullName.trim();

        if (normEmail.isBlank() || !normEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (normName.isBlank()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash is required");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }


        if (userRepository.existsByEmail(normEmail)) {
            throw new IllegalArgumentException("Email already exists");
        }

        try {
            User u = new User();
            u.setEmail(normEmail);
            u.setFullName(normName);
            u.setPasswordHash(passwordHash);
            u.setUserRole(role);


            return userRepository.save(u);
        } catch (DataIntegrityViolationException dup) {

            throw new IllegalArgumentException("Email already exists");
        }
    }
    public User findEmail(String email){
        email = email.trim().toLowerCase();
        return userRepository.findByEmail(email).orElseThrow();

    }
}
