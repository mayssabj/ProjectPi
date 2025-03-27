package tn.esprit.projet_pi.Service;

import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.entity.ValidationMenu;
import tn.esprit.projet_pi.Repository.ValidationMenuRepository;

@Service
public class ValidationMenuServiceImpl implements IValidationMenuService {

    private final ValidationMenuRepository validationMenuRepository;

    public ValidationMenuServiceImpl(ValidationMenuRepository validationMenuRepository) {
        this.validationMenuRepository = validationMenuRepository;
    }

    @Override
    public ValidationMenu validateMenu(ValidationMenu vm) {
        return validationMenuRepository.save(vm);
    }

    @Override
    public ValidationMenu getByMenuId(Long menuId) {
        return validationMenuRepository.findByMenuId(menuId);
    }
}
