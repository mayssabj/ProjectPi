package tn.esprit.projet_pi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.entity.ProfilNutritionnel;
import tn.esprit.projet_pi.Service.IProfilNutritionnelService;

import java.util.List;

@RestController
@RequestMapping("/api/profil-nutritionnel")
public class ProfilNutritionnelController {

    private final IProfilNutritionnelService profilService;

    @Autowired
    public ProfilNutritionnelController(IProfilNutritionnelService profilService) {
        this.profilService = profilService;
    }

    @PostMapping
    public ProfilNutritionnel ajouterProfil(@RequestBody ProfilNutritionnel profil) {
        return profilService.ajouterProfil(profil);
    }

    @PutMapping
    public ProfilNutritionnel mettreAJourProfil(@RequestBody ProfilNutritionnel profil) {
        return profilService.mettreAJourProfil(profil);
    }

    @GetMapping("/user/{userId}")
    public ProfilNutritionnel getProfilParUser(@PathVariable Long userId) {
        return profilService.getProfilParUserId(userId);
    }

    @GetMapping
    public List<ProfilNutritionnel> getTous() {
        return profilService.getTousLesProfils();
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        profilService.supprimerProfil(id);
    }
}
