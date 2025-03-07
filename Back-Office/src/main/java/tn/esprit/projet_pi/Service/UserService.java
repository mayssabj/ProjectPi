package tn.esprit.projet_pi.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Log.JwtService;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public boolean generatePasswordResetToken(String email) {
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
    }

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
