package tn.esprit.projet_pi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.entity.Consultation;
import tn.esprit.projet_pi.Service.IConsultationService;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final IConsultationService consultationService;

    @Autowired
    public ConsultationController(IConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping
    public Consultation ajouter(@RequestBody Consultation consultation) {
        return consultationService.ajouterConsultation(consultation);
    }

    @PutMapping
    public Consultation modifier(@RequestBody Consultation consultation) {
        return consultationService.mettreAJourConsultation(consultation);
    }

    @GetMapping("/{id}")
    public Consultation getOne(@PathVariable Long id) {
        return consultationService.getConsultation(id);
    }

    @GetMapping("/etudiant/{userId}")
    public List<Consultation> getByUser(@PathVariable Long userId) {
        return consultationService.getConsultationsParUser(userId);
    }

    @GetMapping("/medecin/{medecinId}")
    public List<Consultation> getByMedecin(@PathVariable Long medecinId) {
        return consultationService.getConsultationsParMedecin(medecinId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        consultationService.supprimerConsultation(id);
    }
}
