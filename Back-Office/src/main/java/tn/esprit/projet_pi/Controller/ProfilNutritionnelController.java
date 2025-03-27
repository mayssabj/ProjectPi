package tn.esprit.projet_pi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Service.IProfilNutritionnelService;
import tn.esprit.projet_pi.entity.ProfilNutritionnel;

import java.util.List;

@RestController
@RequestMapping("/api/profil")
public class ProfilNutritionnelController {

    private final IProfilNutritionnelService profilService;

    @Autowired
    public ProfilNutritionnelController(IProfilNutritionnelService profilService) {
        this.profilService = profilService;
    }

    @PostMapping("/profil")
    public ResponseEntity<ProfilNutritionnel> create(@RequestBody ProfilNutritionnel profil) {
        ProfilNutritionnel created = profilService.addProfil(profil); // ✅ changement ici
        return ResponseEntity.ok(created);
    }



    @PutMapping("/{id}")
    public ProfilNutritionnel update(@PathVariable Long id, @RequestBody ProfilNutritionnel profil) {
        return profilService.updateProfil(id, profil);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        profilService.deleteProfil(id);
    }

    @GetMapping("/{id}")
    public ProfilNutritionnel getById(@PathVariable Long id) {
        return profilService.getProfilById(id);
    }

    @GetMapping("/user/{userId}")
    public ProfilNutritionnel getByUserId(@PathVariable Long userId) {
        return profilService.getProfilByUserId(userId);
    }

    @GetMapping
    public List<ProfilNutritionnel> getAll() {
        return profilService.getAllProfils();
    }
}
