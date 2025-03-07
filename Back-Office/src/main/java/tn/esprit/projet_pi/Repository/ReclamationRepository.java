package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.projet_pi.entity.Reclamation;
import tn.esprit.projet_pi.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Integer> {
    Reclamation save(Reclamation reclamation);
    List<Reclamation> findAll();
    Reclamation findById(Long idReclamation);
    Optional<Reclamation> deleteById(Long idReclamation);
}
