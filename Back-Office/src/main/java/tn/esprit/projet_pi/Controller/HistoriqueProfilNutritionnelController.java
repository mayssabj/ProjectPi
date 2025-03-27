package tn.esprit.projet_pi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Service.IHistoriqueProfilNutritionnelService;
import tn.esprit.projet_pi.entity.HistoriqueProfilNutritionnel;

import java.util.List;

@RestController
@RequestMapping("/api/historique-profil")
public class HistoriqueProfilNutritionnelController {

    private final IHistoriqueProfilNutritionnelService historiqueService;

    @Autowired
    public HistoriqueProfilNutritionnelController(IHistoriqueProfilNutritionnelService historiqueService) {
        this.historiqueService = historiqueService;
    }

    @PostMapping
    public HistoriqueProfilNutritionnel save(@RequestBody HistoriqueProfilNutritionnel historique) {
        return historiqueService.save(historique);
    }

    @GetMapping("/profil/{profilId}")
    public List<HistoriqueProfilNutritionnel> getByProfil(@PathVariable Long profilId) {
        return historiqueService.getHistoriqueByProfilId(profilId);
    }
}
