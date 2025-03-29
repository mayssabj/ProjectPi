package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.projet_pi.entity.Menu;
import tn.esprit.projet_pi.entity.RegimeAlimentaireType;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    boolean existsByDateAndRegime(LocalDate date, RegimeAlimentaireType regime);

    List<Menu> findByOrderByDateAscIdAsc();

    // Find validated menus
    List<Menu> findByIsValidatedTrue();

    // Find menus for upcoming week
    @Query("SELECT m FROM Menu m WHERE m.date BETWEEN ?1 AND ?2 ORDER BY m.date ASC")
    List<Menu> findMenusForDateRange(LocalDate startDate, LocalDate endDate);

    // Find non-validated menus
    List<Menu> findByIsValidatedFalse();

    // Delete non-validated menus for a date range
    void deleteByIsValidatedFalseAndDateBetween(LocalDate startDate, LocalDate endDate);
    List<Menu> findByIsValidatedFalseAndDateBetween(LocalDate startDate, LocalDate endDate);
}