package tn.esprit.projet_pi.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Log.JwtService;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.security.KeyRep.Type.SECRET;

@Service
public class UserService implements UserInterface{
    @Autowired
    UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password) {
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            User u = user.get();
            if (passwordEncoder.matches(password,u.getMdp()))
                return JwtService.generateToken(u);
        }

        return null;

    }

    @Override
    public User register(User user) {
        user.setMdp(passwordEncoder.encode(user.getMdp()));
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(Math.toIntExact(id)).get();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).get();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepo.findByNom(username).get();
    }

    @Override
    public boolean updateUser(Long id, User updatedUser) {
            return userRepo.findByidUser(id).map(user -> {
                user.setNom(updatedUser.getNom());
                user.setEmail(updatedUser.getEmail());
                user.setAge(updatedUser.getAge());
                userRepo.save(user);
                return true;
            }).orElse(false);
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = userRepo.findByidUser(id).get();
        userRepo.delete(user);
        return true;
    }

    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public List<User> getUserByName(String username) {
        return List.of();
    }

    @Override
    public List<User> getUserByRole(String role) {
        return List.of();
    }

    /**
     * Générer un token de réinitialisation de mot de passe.
     */
    public boolean generatePasswordResetToken(String email ) {
        if (email == null || email.isBlank()) {
            System.out.println("Email invalide ou vide.");
            return false;
        }

        email = email.trim().toLowerCase();
        System.out.println("Vérification de l'email : '" + email + "'");

        Optional<User> userOptional = userRepo.findByEmailIgnoreCase(email);
        if (userOptional.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé avec cet e-mail.");
            return false;
        }

        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepo.save(user);

        System.out.println("Token généré pour l'email : " + email + " -> " + token);

        return true;
    }



    /**
     * Réinitialiser le mot de passe en utilisant un token valide.
     */
    public boolean resetPassword(String token, String newPassword) {
        try {
            // Décodez le token JWT
            Claims claims = Jwts.parser()
                    .setSigningKey(String.valueOf(SECRET))  // Assurez-vous d'utiliser la même clé secrète
                    .parseClaimsJws(token)  // Cette ligne décode le token
                    .getBody();  // Extraire le corps du token

            String email = claims.getSubject();  // L'email dans le token
            Date expiration = claims.getExpiration();  // Date d'expiration du token

            // Vérifier si le token est expiré
            if (expiration.before(new Date())) {
                return false;  // Token expiré
            }

            // Récupérer l'utilisateur avec l'email extrait du token
            Optional<User> userOpt = userRepo.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setMdp(passwordEncoder.encode(newPassword));  // Hachage du mot de passe
                user.setResetToken(null);  // Supprimer le token après utilisation
                user.setResetTokenExpiry(null);  // Supprimer la date d'expiration
                userRepo.save(user);  // Sauvegarder l'utilisateur avec le nouveau mot de passe
                return true;
            }
        } catch (SignatureException e) {
            System.out.println("Erreur de signature du token : " + e.getMessage());
            return false;  // Erreur de signature
        } catch (Exception e) {
            System.out.println("Erreur de parsing du token : " + e.getMessage());
            return false;  // Autres erreurs de parsing
        }
        return false;
    }


    public User findByVerificationToken(String token) {
        return userRepo.findByVerificationToken(token);
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    /*public boolean generatePasswordResetToken(String email) {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = UUID.randomUUID().toString(); // Générer un token unique
            user.setResetToken(token);
            userRepo.save(user);
            // TODO: Envoyer un e-mail avec le lien contenant le token
            System.out.println("Lien de réinitialisation : http://localhost:8081/api/auth/reset-password?token=" + token);

            return true;
        }
        return false;
    }*/

    /*public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOpt = userRepo.findByResetToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setMdp(newPassword); // Hachage recommandé avec BCrypt
            user.setResetToken(null); // Supprimer le token après utilisation
            userRepo.save(user);
            return true;
        }
        return false;
    }*/
}
