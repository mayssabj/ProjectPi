package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.projet_pi.entity.CategoriePlat;
import tn.esprit.projet_pi.entity.Plat;
import tn.esprit.projet_pi.entity.User;

import java.util.List;

@Repository
public interface PlatRepository extends JpaRepository<Plat, Long> {
    List<Plat> findByCategorie(CategoriePlat categorie);


}