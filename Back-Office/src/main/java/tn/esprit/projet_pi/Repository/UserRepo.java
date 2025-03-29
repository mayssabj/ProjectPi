package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.projet_pi.entity.Role;
import tn.esprit.projet_pi.entity.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Optional<User> findByNom(String nom);
    Optional<User> findByidUser(Long idUser);

    Optional<User> findByRole(Role role);
}
