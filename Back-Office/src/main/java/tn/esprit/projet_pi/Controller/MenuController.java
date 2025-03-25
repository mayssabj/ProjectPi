package tn.esprit.projet_pi.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.Service.MenuService;
import tn.esprit.projet_pi.Service.MenuServiceImpl;
import tn.esprit.projet_pi.entity.Menu;
import tn.esprit.projet_pi.entity.Role;
import tn.esprit.projet_pi.entity.User;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin("*")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuServiceImpl menuServiceImpl;
    @Autowired
    private UserRepo userRepository;

    // Endpoint pour générer les menus de la semaine
    @PostMapping("/generate")
    public ResponseEntity<String> generateMenus(@RequestParam Long userId) {
        User user = userRepository.findByidUser(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé : seuls les Staff peuvent générer des menus");
        }

        try {
            menuService.generateWeeklyMenus(userId); // ✅ Pass userId to ensure `createdBy` is set
            return ResponseEntity.ok("Menus générés avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la génération des menus");
        }
    }


    //    @GetMapping("/week")
//    public ResponseEntity<List<Menu>> getMenusDeLaSemaine() {
//        List<Menu> menus = menuService.getMenusDeLaSemaine();
//        return ResponseEntity.ok(menus);
//    }
    @GetMapping
    public List<Menu> getMenus() {
        return menuService.getAllMenus();
    }

}