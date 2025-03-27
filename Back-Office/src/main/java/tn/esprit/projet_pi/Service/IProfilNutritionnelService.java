package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.ProfilNutritionnel;

import java.util.List;

public interface IProfilNutritionnelService {
    ProfilNutritionnel addProfil(ProfilNutritionnel profil);
    ProfilNutritionnel updateProfil(Long id, ProfilNutritionnel profil);
    void deleteProfil(Long id);
    ProfilNutritionnel getProfilById(Long id);
    ProfilNutritionnel getProfilByUserId(Long userId);
    List<ProfilNutritionnel> getAllProfils();
}
