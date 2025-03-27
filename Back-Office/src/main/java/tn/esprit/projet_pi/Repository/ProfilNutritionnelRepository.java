package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.projet_pi.entity.ProfilNutritionnel;

public interface ProfilNutritionnelRepository extends JpaRepository<ProfilNutritionnel, Long> {
    ProfilNutritionnel findByUser_IdUser(Long userId);
}
