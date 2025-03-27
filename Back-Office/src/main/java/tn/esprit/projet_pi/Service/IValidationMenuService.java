package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.ValidationMenu;

public interface IValidationMenuService {
    ValidationMenu validateMenu(ValidationMenu vm);
    ValidationMenu getByMenuId(Long menuId);
}
