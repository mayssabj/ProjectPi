package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.Consultation;

import java.util.List;

public interface IConsultationService {
    Consultation addConsultation(Consultation c);
    Consultation updateConsultation(Long id, Consultation c);
    void deleteConsultation(Long id);
    Consultation getConsultation(Long id);
    List<Consultation> getByEtudiant(Long userId);
    List<Consultation> getByMedecin(Long medecinId);
}
