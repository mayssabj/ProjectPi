package tn.esprit.projet_pi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.Service.MenuService;
import tn.esprit.projet_pi.entity.Menu;
import tn.esprit.projet_pi.entity.Role;
import tn.esprit.projet_pi.entity.User;

import java.util.List;

@RestController
@RequestMapping("/api/menus")

@CrossOrigin(origins = "http://localhost:4200")

public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private UserRepo userRepository;

    // Endpoint pour générer les menus de la semaine
    @RequestMapping(value="/generate",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateMenus(@RequestParam Long userId) {
        User user = userRepository.findByidUser(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Accès refusé : seuls les Staff peuvent générer des menus");
        }

        try {
            menuService.generateWeeklyMenus(userId);
            return  ResponseEntity.ok("{\"message\": \"Menus générés avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la génération des menus: " + e.getMessage());
        }
    }

    // Endpoint pour récupérer tous les menus (admin/staff uniquement)
    @GetMapping("/all")
    public ResponseEntity<?> getAllMenus(@RequestParam Long userId) {
        User user = userRepository.findByidUser(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff) && !user.getRole().equals(Role.Admin) &&
                !user.getRole().equals(Role.Medecin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Accès refusé : accès restreint");
        }

        List<Menu> menus = menuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    // Endpoint pour récupérer uniquement les menus validés (accessible à tous)
    @GetMapping
    public ResponseEntity<List<Menu>> getValidatedMenus() {
        List<Menu> validatedMenus = menuService.getValidatedMenus();
        return ResponseEntity.ok(validatedMenus);
    }

    // Endpoint pour qu'un médecin valide des menus
    @PostMapping("/validate")
    public ResponseEntity<?> validateMenus(@RequestParam Long doctorId,
                                           @RequestBody List<Long> menuIds) {
        User doctor = userRepository.findByidUser(doctorId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!doctor.getRole().equals(Role.Medecin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Accès refusé : seuls les médecins peuvent valider les menus");
        }

        try {
            menuService.validateMenus(doctorId, menuIds);
            return ResponseEntity.ok("Menus validés avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la validation des menus: " + e.getMessage());
        }
    }

    // Endpoint pour régénérer les menus non validés
    @RequestMapping(value="/regenerate",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> regenerateMenus(@RequestParam Long userId) {
        User user = userRepository.findByidUser(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Accès refusé : seuls les Staff peuvent régénérer des menus");
        }

        try {
            menuService.regenerateWeeklyMenus(userId);
            return  ResponseEntity.ok("{\"message\": \"Menus générés avec succès\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la régénération des menus: " + e.getMessage());
        }
    }
}