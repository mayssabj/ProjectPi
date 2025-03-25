package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.projet_pi.entity.Consultation;

import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    @Query("SELECT c FROM Consultation c WHERE c.user.idUser = :userId")
    List<Consultation> findByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM Consultation c WHERE c.medecin.idUser = :medecinId")
    List<Consultation> findByMedecinId(@Param("medecinId") Long medecinId);

}
