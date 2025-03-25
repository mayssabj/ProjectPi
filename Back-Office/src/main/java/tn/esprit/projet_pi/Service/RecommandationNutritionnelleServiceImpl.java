package tn.esprit.projet_pi.Service;

import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.entity.RecommandationNutritionnelle;
import tn.esprit.projet_pi.Repository.RecommandationNutritionnelleRepository;

import java.util.List;

@Service
public class RecommandationNutritionnelleServiceImpl implements IRecommandationNutritionnelleService {

    private final RecommandationNutritionnelleRepository recommandationRepo;

    public RecommandationNutritionnelleServiceImpl(RecommandationNutritionnelleRepository recommandationRepo) {
        this.recommandationRepo = recommandationRepo;
    }

    @Override
    public RecommandationNutritionnelle ajouterRecommandation(RecommandationNutritionnelle recommandation) {
        return recommandationRepo.save(recommandation);
    }

    @Override
    public RecommandationNutritionnelle mettreAJourRecommandation(RecommandationNutritionnelle recommandation) {
        return recommandationRepo.save(recommandation);
    }

    @Override
    public List<RecommandationNutritionnelle> getParConsultation(Long consultationId) {
        return recommandationRepo.findByConsultationId(consultationId);
    }

    @Override
    public List<RecommandationNutritionnelle> getParMedecin(Long medecinId) {
        return recommandationRepo.findByMedecinId(medecinId);
    }

    @Override
    public void supprimerRecommandation(Long id) {
        recommandationRepo.deleteById(id);
    }
}
