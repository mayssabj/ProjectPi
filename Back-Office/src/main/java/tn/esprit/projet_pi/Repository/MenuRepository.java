package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.projet_pi.entity.Menu;
import tn.esprit.projet_pi.entity.RegimeAlimentaireType;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    // List<Menu> findByDateBetween(LocalDate startDate, LocalDate endDate);
    boolean existsByDateAndRegime(LocalDate date, RegimeAlimentaireType regime);

    List<Menu> findByOrderByDateAscIdAsc();


}
