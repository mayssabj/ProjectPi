package tn.esprit.projet_pi.Service;

import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.entity.HistoriqueProfilNutritionnel;
import tn.esprit.projet_pi.Repository.HistoriqueProfilNutritionnelRepository;

import java.util.List;

@Service
public class HistoriqueProfilNutritionnelServiceImpl implements IHistoriqueProfilNutritionnelService {

    private final HistoriqueProfilNutritionnelRepository historiqueProfilNutritionnelRepository;

    public HistoriqueProfilNutritionnelServiceImpl(HistoriqueProfilNutritionnelRepository historiqueProfilNutritionnelRepository) {
        this.historiqueProfilNutritionnelRepository = historiqueProfilNutritionnelRepository;
    }

    @Override
    public HistoriqueProfilNutritionnel save(HistoriqueProfilNutritionnel historique) {
        return historiqueProfilNutritionnelRepository.save(historique);
    }

    @Override
    public List<HistoriqueProfilNutritionnel> getHistoriqueByProfilId(Long profilId) {
        return historiqueProfilNutritionnelRepository.findByProfil_IdOrderByDateEnregistrementDesc(profilId); // ✅ corrigé
    }
}
