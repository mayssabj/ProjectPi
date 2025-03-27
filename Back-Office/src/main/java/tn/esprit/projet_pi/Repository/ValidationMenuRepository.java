package tn.esprit.projet_pi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.projet_pi.entity.ValidationMenu;

public interface ValidationMenuRepository extends JpaRepository<ValidationMenu, Long> {
    ValidationMenu findByMenuId(Long menuId);
}