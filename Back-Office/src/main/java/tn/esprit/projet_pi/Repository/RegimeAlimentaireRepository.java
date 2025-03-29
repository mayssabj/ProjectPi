package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.projet_pi.entity.RegimeAlimentaire;
import tn.esprit.projet_pi.entity.RegimeAlimentaireType;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegimeAlimentaireRepository extends JpaRepository<RegimeAlimentaire, Long> {
    Optional<RegimeAlimentaire> findFirstByType(RegimeAlimentaireType type);

    RegimeAlimentaire findByType(RegimeAlimentaireType type);
//    @Query("SELECT r FROM RegimeAlimentaire r LEFT JOIN FETCH r.platsRecommandes")
//    List<RegimeAlimentaire> findAllWithPlats();



}