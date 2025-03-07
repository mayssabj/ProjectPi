package tn.esprit.projet_pi.Controller;

import jakarta.persistence.Entity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Log.LoginRequest;
import tn.esprit.projet_pi.Log.RegisterRequest;
import tn.esprit.projet_pi.Service.UserService;
import tn.esprit.projet_pi.entity.User;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        user.setMdp(request.getMdp());
        user.setRole(request.getRole());
        user.setAge(request.getAge());
        User registeredUser = userService.register(user);
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping("/login")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
        String token = String.valueOf(userService.login(loginRequest.getEmail(), loginRequest.getMdp()));

        // Renvoyer le token dans une réponse JSON
        return ResponseEntity.ok(Collections.singletonMap("token", token)); // Utilisation d'une map pour inclure le token dans une structure JSON
    }
    @DeleteMapping("/user_del/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete user.");
        }
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    @PutMapping("/userUpdate/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        boolean updated = userService.updateUser(id, updatedUser);
        if (updated) {
            return ResponseEntity.ok("User updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("User not found or update failed.");
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        boolean result = userService.generatePasswordResetToken(email);
        if (result) {
            return ResponseEntity.ok("Un lien de réinitialisation a été envoyé à votre e-mail.");
        } else {
            return ResponseEntity.badRequest().body("Aucun utilisateur trouvé avec cet e-mail.");
        }
    }


}
