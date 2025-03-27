package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.RecommandationNutritionnelle;

import java.util.List;

public interface IRecommandationNutritionnelleService {
    RecommandationNutritionnelle add(RecommandationNutritionnelle r);
    void delete(Long id);
    List<RecommandationNutritionnelle> getByConsultation(Long consultationId);
    List<RecommandationNutritionnelle> getByMedecin(Long medecinId);
}
