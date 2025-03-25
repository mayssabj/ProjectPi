package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.projet_pi.entity.RecommandationNutritionnelle;

import java.util.List;

public interface RecommandationNutritionnelleRepository extends JpaRepository<RecommandationNutritionnelle, Long> {

    @Query("SELECT r FROM RecommandationNutritionnelle r WHERE r.consultation.consultationId = :id")
    List<RecommandationNutritionnelle> findByConsultationId(@Param("id") Long consultationId);

    @Query("SELECT r FROM RecommandationNutritionnelle r WHERE r.medecin.idUser = :id")
    List<RecommandationNutritionnelle> findByMedecinId(@Param("id") Long medecinId);
}
