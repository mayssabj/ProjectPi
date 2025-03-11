package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.projet_pi.entity.Produit;
@Repository
public interface ProduitRepository extends JpaRepository<Produit,Integer> {

}
