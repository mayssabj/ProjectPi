package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.projet_pi.entity.PlatProduit;

@Repository
public interface PlatProduitRepository extends JpaRepository<PlatProduit, Long> {
}