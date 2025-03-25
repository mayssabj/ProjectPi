package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.projet_pi.entity.ValidationMenu;

import java.util.List;

public interface ValidationMenuRepository extends JpaRepository<ValidationMenu, Long> {
    @Query("SELECT v FROM ValidationMenu v WHERE v.medecin.idUser = :id")
    List<ValidationMenu> findByMedecinId(@Param("id") Long id);
    ValidationMenu findByMenuId(Long menuId);
}
