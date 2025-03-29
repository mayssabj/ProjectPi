package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.projet_pi.entity.Reclamation;
import tn.esprit.projet_pi.entity.ReclamationStatus;
import tn.esprit.projet_pi.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Integer> {
    Reclamation save(Reclamation reclamation);
    List<Reclamation> findAll();
    //Reclamation findById(Long idReclamation); // Fix return type
    Optional<Reclamation> deleteById(Long idReclamation);
    List<Reclamation> findByUser_IdUser(Long idUser);

    // New method to find unresponded reclamations
    List<Reclamation> findByStatusAndDateCreatedBefore(
            ReclamationStatus status,
            LocalDateTime dateThreshold
    );

    @Query("SELECT r FROM Reclamation r WHERE r.user.idUser = :idUser")
    List<Reclamation> findByUserId(@Param("idUser") Long idUser);
}
