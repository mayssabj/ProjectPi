package tn.esprit.projet_pi.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Service.IConsultationService;
import tn.esprit.projet_pi.entity.Consultation;

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
    public ResponseEntity<Consultation> create(@RequestBody Consultation consultation) {
        Consultation created = consultationService.addConsultation(consultation);
        return ResponseEntity.ok(created);
    }


    @PutMapping("/{id}")
    public Consultation update(@PathVariable Long id, @RequestBody Consultation consultation) {
        return consultationService.updateConsultation(id, consultation);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        consultationService.deleteConsultation(id);
    }

    @GetMapping("/{id}")
    public Consultation get(@PathVariable Long id) {
        return consultationService.getConsultation(id);
    }

    @GetMapping("/etudiant/{userId}")
    public List<Consultation> getByEtudiant(@PathVariable Long userId) {
        return consultationService.getByEtudiant(userId);
    }

    @GetMapping("/medecin/{medecinId}")
    public List<Consultation> getByMedecin(@PathVariable Long medecinId) {
        return consultationService.getByMedecin(medecinId);
    }
}
