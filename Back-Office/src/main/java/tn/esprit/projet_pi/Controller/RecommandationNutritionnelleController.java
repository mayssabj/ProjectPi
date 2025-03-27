package tn.esprit.projet_pi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Service.IRecommandationNutritionnelleService;
import tn.esprit.projet_pi.entity.RecommandationNutritionnelle;

import java.util.List;

@RestController
@RequestMapping("/api/recommandations")
public class RecommandationNutritionnelleController {

    private final IRecommandationNutritionnelleService recommandationService;

    @Autowired
    public RecommandationNutritionnelleController(IRecommandationNutritionnelleService recommandationService) {
        this.recommandationService = recommandationService;
    }

    @PostMapping
    public RecommandationNutritionnelle add(@RequestBody RecommandationNutritionnelle r) {
        return recommandationService.add(r);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        recommandationService.delete(id);
    }

    @GetMapping("/consultation/{consultationId}")
    public List<RecommandationNutritionnelle> getByConsultation(@PathVariable Long consultationId) {
        return recommandationService.getByConsultation(consultationId);
    }

    @GetMapping("/medecin/{medecinId}")
    public List<RecommandationNutritionnelle> getByMedecin(@PathVariable Long medecinId) {
        return recommandationService.getByMedecin(medecinId);
    }
}
