package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.projet_pi.entity.RecommandationNutritionnelle;

import java.util.List;

public interface RecommandationNutritionnelleRepository extends JpaRepository<RecommandationNutritionnelle, Long> {
    List<RecommandationNutritionnelle> findByConsultationId(Long consultationId);
    List<RecommandationNutritionnelle> findByMedecin_IdUser(Long medecinId);

}