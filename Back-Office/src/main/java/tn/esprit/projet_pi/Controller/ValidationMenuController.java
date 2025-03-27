package tn.esprit.projet_pi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Service.IValidationMenuService;
import tn.esprit.projet_pi.entity.ValidationMenu;

@RestController
@RequestMapping("/api/validation-menu")
public class ValidationMenuController {

    private final IValidationMenuService validationMenuService;

    @Autowired
    public ValidationMenuController(IValidationMenuService validationMenuService) {
        this.validationMenuService = validationMenuService;
    }

    @PostMapping
    public ValidationMenu validate(@RequestBody ValidationMenu vm) {
        return validationMenuService.validateMenu(vm);
    }

    @GetMapping("/menu/{menuId}")
    public ValidationMenu getByMenu(@PathVariable Long menuId) {
        return validationMenuService.getByMenuId(menuId);
    }
}
