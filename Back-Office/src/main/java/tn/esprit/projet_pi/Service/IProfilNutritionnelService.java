package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.ProfilNutritionnel;

import java.util.List;

public interface IProfilNutritionnelService {
    ProfilNutritionnel ajouterProfil(ProfilNutritionnel profil);
    ProfilNutritionnel mettreAJourProfil(ProfilNutritionnel profil);
    ProfilNutritionnel getProfilParUserId(Long userId);
    List<ProfilNutritionnel> getTousLesProfils();
    void supprimerProfil(Long id);
}