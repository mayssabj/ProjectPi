package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.projet_pi.entity.ProduitHistorique;

public interface ProduitHistoriqueRepository extends JpaRepository<ProduitHistorique, Integer> {
}
