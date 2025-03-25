package tn.esprit.projet_pi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.entity.RecommandationNutritionnelle;
import tn.esprit.projet_pi.Service.IRecommandationNutritionnelleService;

import java.util.List;

@RestController
@RequestMapping("/api/recommandations")
public class RecommandationNutritionnelleController {

    public final IRecommandationNutritionnelleService recommandationService;

    @Autowired
    public RecommandationNutritionnelleController(IRecommandationNutritionnelleService recommandationService) {
        this.recommandationService = recommandationService;
    }

    @PostMapping
    public RecommandationNutritionnelle ajouter(@RequestBody RecommandationNutritionnelle r) {
        return recommandationService.ajouterRecommandation(r);
    }

    @PutMapping
    public RecommandationNutritionnelle modifier(@RequestBody RecommandationNutritionnelle r) {
        return recommandationService.mettreAJourRecommandation(r);
    }

    @GetMapping("/consultation/{consultationId}")
    public List<RecommandationNutritionnelle> getByConsultation(@PathVariable Long consultationId) {
        return recommandationService.getParConsultation(consultationId);
    }

    @GetMapping("/medecin/{medecinId}")
    public List<RecommandationNutritionnelle> getByMedecin(@PathVariable Long medecinId) {
        return recommandationService.getParMedecin(medecinId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        recommandationService.supprimerRecommandation(id);
    }
}
