package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.projet_pi.entity.ProfilNutritionnel;

public interface ProfilNutritionnelRepository extends JpaRepository<ProfilNutritionnel, Long> {
    @Query("SELECT p FROM ProfilNutritionnel p WHERE p.user.idUser = :userId")
    ProfilNutritionnel findByUserId(@Param("userId") Long userId);
}
