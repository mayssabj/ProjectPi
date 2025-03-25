package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.ValidationMenu;

import java.util.List;

public interface IValidationMenuService {
    ValidationMenu validerMenu(ValidationMenu validation);
    ValidationMenu getValidationParMenu(Long menuId);
    List<ValidationMenu> getParMedecin(Long medecinId);
    void supprimerValidation(Long id);
}

