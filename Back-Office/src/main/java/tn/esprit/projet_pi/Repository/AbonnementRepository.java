package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.projet_pi.entity.Abonnement;

import java.util.Optional;


public interface AbonnementRepository  extends JpaRepository<Abonnement, Long> {
    Optional<Abonnement> findByConfirmationCode(String confirmationCode);
}
