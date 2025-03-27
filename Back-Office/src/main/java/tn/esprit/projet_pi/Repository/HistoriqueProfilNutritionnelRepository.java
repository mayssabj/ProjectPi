package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.projet_pi.entity.HistoriqueProfilNutritionnel;

import java.util.List;

public interface HistoriqueProfilNutritionnelRepository extends JpaRepository<HistoriqueProfilNutritionnel, Long> {
    List<HistoriqueProfilNutritionnel> findByProfil_IdOrderByDateEnregistrementDesc(Long profilId);
}