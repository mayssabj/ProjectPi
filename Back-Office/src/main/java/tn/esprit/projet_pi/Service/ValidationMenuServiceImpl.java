package tn.esprit.projet_pi.Service;

import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.entity.ValidationMenu;
import tn.esprit.projet_pi.Repository.ValidationMenuRepository;

import java.util.List;

@Service
public class ValidationMenuServiceImpl implements IValidationMenuService {

    private final ValidationMenuRepository validationRepo;

    public ValidationMenuServiceImpl(ValidationMenuRepository validationRepo) {
        this.validationRepo = validationRepo;
    }

    @Override
    public ValidationMenu validerMenu(ValidationMenu validation) {
        return validationRepo.save(validation);
    }

    @Override
    public ValidationMenu getValidationParMenu(Long menuId) {
        return validationRepo.findByMenuId(menuId);
    }

    @Override
    public List<ValidationMenu> getParMedecin(Long medecinId) {
        return validationRepo.findByMedecinId(medecinId);
    }

    @Override
    public void supprimerValidation(Long id) {
        validationRepo.deleteById(id);
    }

}
