package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.Consultation;

import java.util.List;

public interface IConsultationService {
    Consultation ajouterConsultation(Consultation consultation);
    Consultation mettreAJourConsultation(Consultation consultation);
    Consultation getConsultation(Long id);
    List<Consultation> getConsultationsParUser(Long userId);
    List<Consultation> getConsultationsParMedecin(Long medecinId);
    void supprimerConsultation(Long id);
}
