package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.HistoriqueProfilNutritionnel;

import java.util.List;

public interface IHistoriqueProfilNutritionnelService {
    HistoriqueProfilNutritionnel save(HistoriqueProfilNutritionnel historique);
    List<HistoriqueProfilNutritionnel> getHistoriqueByProfilId(Long profilId);
}
