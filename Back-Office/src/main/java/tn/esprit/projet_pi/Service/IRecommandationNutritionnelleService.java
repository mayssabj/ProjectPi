package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.RecommandationNutritionnelle;

import java.util.List;

public interface IRecommandationNutritionnelleService {
    RecommandationNutritionnelle ajouterRecommandation(RecommandationNutritionnelle recommandation);
    RecommandationNutritionnelle mettreAJourRecommandation(RecommandationNutritionnelle recommandation);
    List<RecommandationNutritionnelle> getParConsultation(Long consultationId);
    List<RecommandationNutritionnelle> getParMedecin(Long medecinId);
    void supprimerRecommandation(Long id);
}
