package tn.esprit.projet_pi.Controller;

import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Log.JwtService;
import tn.esprit.projet_pi.Log.LoginRequest;
import tn.esprit.projet_pi.Log.RegisterRequest;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.Service.EmailService;
import tn.esprit.projet_pi.Service.UserService;
import tn.esprit.projet_pi.entity.ForgotPasswordRequest;
import tn.esprit.projet_pi.entity.User;

import java.nio.charset.Charset;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final EmailService emailService;
    private final UserRepo userRepo;
    private final JwtService jwtService;

    public AuthController(UserService userService, EmailService emailService, UserRepo userRepo, JwtService jwtService) {
        this.userService = userService;
        this.emailService = emailService;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        user.setMdp(request.getMdp());
        user.setRole(request.getRole());
        user.setAge(request.getAge());

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);

        User registeredUser = userService.register(user);
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);

        return ResponseEntity.ok("Utilisateur inscrit avec succès. Veuillez vérifier votre e-mail.");

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
    /*
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        boolean result = userService.generatePasswordResetToken(email);
        if (result) {
            return ResponseEntity.ok("Un lien de réinitialisation a été envoyé à votre e-mail.");
        } else {
            return ResponseEntity.badRequest().body("Aucun utilisateur trouvé avec cet e-mail.");
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest request) {
        String email = request.getEmail();
        JwtService jwtService = new JwtService();
        User user = userService.getUserByEmail(email);
        String token = jwtService.generateToken(user);
        System.out.println("Email reçu dans la requête : '" + email + "'");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("L'email est vide ou invalide.");
        }

        boolean result = userService.generatePasswordResetToken(email);
        if (result) {
            emailService.sendResetPasswordEmail(email, token);
            return ResponseEntity.ok("Un lien de réinitialisation a été envoyé à votre e-mail.");

        } else {
            return ResponseEntity.badRequest().body("Aucun utilisateur trouvé avec cet e-mail.");
        }
    }

    /**
     * Endpoint pour réinitialiser le mot de passe avec un token.

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        System.out.println("Received token: " + token);  // Debugging print to check if token is correct
        boolean result = userService.resetPassword(token, newPassword);
        if (result) {
            return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
        } else {
            return ResponseEntity.badRequest().body("Token invalide ou expiré.");
        }
    }
*/
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String email = request.getEmail(); // On récupère l'email de l'objet envoyé

        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            emailService.sendResetPasswordEmail(user.getEmail().toString(),jwtService.generateToken(user)); // Envoi de l'email de réinitialisation
            return ResponseEntity.ok(Collections.singletonMap("message", "Un lien de réinitialisation a été envoyé à votre adresse email."));
        }
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Aucun utilisateur trouvé avec cet email."));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        String email = jwtService.extractUsername(token); // Extraction de l'email du token
        Optional<User> userOptional = userRepo.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Utilisateur introuvable.");
        }

        User user = userOptional.get();

        // Optionnel : vous pouvez vérifier que le token n'est pas expiré, selon votre logique


        user.setMdp(passwordEncoder.encode(newPassword)); // Encodage du nouveau mot de passe
        userRepo.save(user); // Sauvegarde du mot de passe réinitialisé

        return ResponseEntity.ok("Votre mot de passe a été réinitialisé avec succès.");
    }
    @PostMapping("/test-email")
    public ResponseEntity<String> testEmail() {
        try {
            emailService.sendResetPasswordEmail("benmassoudrayen7@gmail.com", "test-token");
            return ResponseEntity.ok("Email envoyé avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        User user = userService.findByVerificationToken(token);
        if (user == null) {
            return ResponseEntity.badRequest().body("Token invalide !");
        }

        user.setIs_verified(true);
        user.setVerificationToken(null);
        userService.saveUser(user);

        return ResponseEntity.ok("Email vérifié avec succès !");
    }


}
