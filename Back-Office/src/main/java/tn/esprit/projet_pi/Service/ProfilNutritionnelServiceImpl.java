package tn.esprit.projet_pi.Service;

import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.entity.ProfilNutritionnel;
import tn.esprit.projet_pi.Repository.ProfilNutritionnelRepository;

import java.util.List;

@Service
public class ProfilNutritionnelServiceImpl implements IProfilNutritionnelService {

    private final ProfilNutritionnelRepository profilRepo;

    public ProfilNutritionnelServiceImpl(ProfilNutritionnelRepository profilRepo) {
        this.profilRepo = profilRepo;
    }

    @Override
    public ProfilNutritionnel ajouterProfil(ProfilNutritionnel profil) {
        return profilRepo.save(profil);
    }

    @Override
    public ProfilNutritionnel mettreAJourProfil(ProfilNutritionnel profil) {
        return profilRepo.save(profil);
    }

    @Override
    public ProfilNutritionnel getProfilParUserId(Long userId) {
        return profilRepo.findByUserId(userId);
    }

    @Override
    public List<ProfilNutritionnel> getTousLesProfils() {
        return profilRepo.findAll();
    }

    @Override
    public void supprimerProfil(Long id) {
        profilRepo.deleteById(id);
    }
}
