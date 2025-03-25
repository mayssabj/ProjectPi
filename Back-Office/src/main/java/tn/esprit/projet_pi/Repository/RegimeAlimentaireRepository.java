package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.projet_pi.entity.RegimeAlimentaire;
import tn.esprit.projet_pi.entity.RegimeAlimentaireType;

import java.util.Optional;

@Repository
public interface RegimeAlimentaireRepository extends JpaRepository<RegimeAlimentaire, Long> {
    Optional<RegimeAlimentaire> findFirstByType(RegimeAlimentaireType type);


    Optional<RegimeAlimentaire> findByType(RegimeAlimentaireType regime);
}
