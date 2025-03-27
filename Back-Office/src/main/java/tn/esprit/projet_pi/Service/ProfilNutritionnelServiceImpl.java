package tn.esprit.projet_pi.Service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Repository.ProfilNutritionnelRepository;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.ProfilNutritionnel;
import tn.esprit.projet_pi.entity.User;

import java.util.List;

@Service
public class ProfilNutritionnelServiceImpl implements IProfilNutritionnelService {

    private final ProfilNutritionnelRepository profilNutritionnelRepository;
    private final UserRepo userRepo;

    public ProfilNutritionnelServiceImpl(ProfilNutritionnelRepository profilNutritionnelRepository, UserRepo userRepo) {
        this.profilNutritionnelRepository = profilNutritionnelRepository;
        this.userRepo = userRepo;
    }

    @Override
    public ProfilNutritionnel addProfil(ProfilNutritionnel profil) {
        // 👇 Récupère l'email du user à partir du JWT (via le contexte de sécurité)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 👇 Recherche le User avec cet email
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email : " + email));

        // 👇 Lier l'utilisateur récupéré au profil nutritionnel
        profil.setUser(user);

        return profilNutritionnelRepository.save(profil);
    }




    @Override
    public ProfilNutritionnel updateProfil(Long id, ProfilNutritionnel profil) {
        profil.setId(id);
        return profilNutritionnelRepository.save(profil);
    }

    @Override
    public void deleteProfil(Long id) {
        profilNutritionnelRepository.deleteById(id);
    }

    @Override
    public ProfilNutritionnel getProfilById(Long id) {
        return profilNutritionnelRepository.findById(id).orElse(null);
    }

    @Override
    public ProfilNutritionnel getProfilByUserId(Long userId) {
        return profilNutritionnelRepository.findByUser_IdUser(userId);
    }

    @Override
    public List<ProfilNutritionnel> getAllProfils() {
        return profilNutritionnelRepository.findAll();
    }
}
