package tn.esprit.projet_pi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.entity.ValidationMenu;
import tn.esprit.projet_pi.Service.IValidationMenuService;

import java.util.List;

@RestController
@RequestMapping("/api/validation-menu")
public class ValidationMenuController {

    private final IValidationMenuService validationService;

    @Autowired
    public ValidationMenuController(IValidationMenuService validationService) {
        this.validationService = validationService;
    }

    @PostMapping
    public ValidationMenu valider(@RequestBody ValidationMenu validation) {
        return validationService.validerMenu(validation);
    }

    @GetMapping("/menu/{menuId}")
    public ValidationMenu getByMenu(@PathVariable Long menuId) {
        return validationService.getValidationParMenu(menuId);
    }

    @GetMapping("/nutritionniste/{id}")
    public List<ValidationMenu> getByNutritionniste(@PathVariable Long id) {
        return validationService.getParMedecin(id);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        validationService.supprimerValidation(id);
    }
}
