package tn.esprit.projet_pi.Security;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Repository.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepository;

    public CustomUserDetailsService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        tn.esprit.projet_pi.entity.User user = (tn.esprit.projet_pi.entity.User) userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Si le rôle est déjà correctement formaté dans la base, vous pouvez simplement l'utiliser
        String role = user.getRole().name(); // Assurez-vous que le rôle dans l'entity est bien sans "ROLE_"

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getMdp())
                .roles(role) // Utilisez le rôle sans ajouter "ROLE_"
                .build();
    }


}

