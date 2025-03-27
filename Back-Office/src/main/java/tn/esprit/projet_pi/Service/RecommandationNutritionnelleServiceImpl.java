package tn.esprit.projet_pi.Service;

import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.entity.RecommandationNutritionnelle;
import tn.esprit.projet_pi.Repository.RecommandationNutritionnelleRepository;

import java.util.List;

@Service
public class RecommandationNutritionnelleServiceImpl implements IRecommandationNutritionnelleService {

    private final RecommandationNutritionnelleRepository recommandationNutritionnelleRepository;

    public RecommandationNutritionnelleServiceImpl(RecommandationNutritionnelleRepository recommandationNutritionnelleRepository) {
        this.recommandationNutritionnelleRepository = recommandationNutritionnelleRepository;
    }

    @Override
    public RecommandationNutritionnelle add(RecommandationNutritionnelle r) {
        return recommandationNutritionnelleRepository.save(r);
    }

    @Override
    public void delete(Long id) {
        recommandationNutritionnelleRepository.deleteById(id);
    }

    @Override
    public List<RecommandationNutritionnelle> getByConsultation(Long consultationId) {
        return recommandationNutritionnelleRepository.findByConsultationId(consultationId);
    }

    @Override
    public List<RecommandationNutritionnelle> getByMedecin(Long medecinId) {
        return recommandationNutritionnelleRepository.findByMedecin_IdUser(medecinId); // ✅ Correction ici
    }
}
