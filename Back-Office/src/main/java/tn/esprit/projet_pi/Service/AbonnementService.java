package tn.esprit.projet_pi.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Repository.AbonnementRepository;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.Abonnement;
import tn.esprit.projet_pi.entity.User;
import tn.esprit.projet_pi.interfaces.IAbonnement;

@Service
public class AbonnementService implements IAbonnement {

    final AbonnementRepository abonnementRepository;
    final UserRepo userRepo;

    @Autowired
    public AbonnementService(AbonnementRepository abonnementRepository, UserRepo userRepo) {
        this.abonnementRepository = abonnementRepository;
        this.userRepo = userRepo;
    }

    @Override
    public Abonnement createAbonnementByUser(Abonnement abonnement, Long userId) {
        User user = userRepo.findByidUser(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));
        if (user.getAbonnement() != null) {
            throw new RuntimeException("L'utilisateur a déjà un abonnement.");
        }
        abonnement.setUser(user);
        user.setAbonnement(abonnement);
        return abonnementRepository.save(abonnement);
    }


    @Override
    public void deleteAbonnement(Long userId, Long idAbonnement) {
        Abonnement abonnement = abonnementRepository.findById(idAbonnement).orElse(null);
        if (abonnement != null && abonnement.getUser().getId_user().equals(userId)) {
            abonnementRepository.delete(abonnement);
        }
    }

    @Override
    public Abonnement updateAbonnement(Long userId, Abonnement updatedAbonnement) {
        // Fetch the existing abonnement
        Abonnement existingAbonnement = abonnementRepository.findById(updatedAbonnement.getIdAbonnement())
                .orElseThrow(() -> new RuntimeException("Abonnement not found with ID: " + updatedAbonnement.getIdAbonnement()));

        // Check if the abonnement belongs to the user
        if (!existingAbonnement.getUser().getId_user().equals(userId)) {
            throw new RuntimeException("Abonnement does not belong to user with ID: " + userId);
        }

        // Update only non-null fields from the updatedAbonnement
        if (updatedAbonnement.getTypeAbonnement() != null) {
            existingAbonnement.setTypeAbonnement(updatedAbonnement.getTypeAbonnement());
        }
        if (updatedAbonnement.getAbonnementStatus() != null) {
            existingAbonnement.setAbonnementStatus(updatedAbonnement.getAbonnementStatus());
        }
        if (updatedAbonnement.getRenouvellementAutomatique() != null) {
            existingAbonnement.setRenouvellementAutomatique(updatedAbonnement.getRenouvellementAutomatique());
        }
        if (updatedAbonnement.getDateDebut() != null) {
            existingAbonnement.setDateDebut(updatedAbonnement.getDateDebut());
        }
        if (updatedAbonnement.getDateFin() != null) {
            existingAbonnement.setDateFin(updatedAbonnement.getDateFin());
        }
        if (updatedAbonnement.getCout() != null) {
            existingAbonnement.setCout(updatedAbonnement.getCout());
        }
        if (updatedAbonnement.getEnPeriodeEssai() != null) {
            existingAbonnement.setEnPeriodeEssai(updatedAbonnement.getEnPeriodeEssai());
        }
        if (updatedAbonnement.getDateFinEssai() != null) {
            existingAbonnement.setDateFinEssai(updatedAbonnement.getDateFinEssai());
        }

        // Save and return the updated abonnement
        return abonnementRepository.save(existingAbonnement);
    }


    @Override
    public Abonnement getAbonnementById(Long userId, Long idAbonnement) {
        Abonnement abonnement = abonnementRepository.findById(idAbonnement).orElse(null);
        if (abonnement != null && abonnement.getUser().getId_user().equals(userId)) {
            return abonnement;
        }
        return null;
    }
}
