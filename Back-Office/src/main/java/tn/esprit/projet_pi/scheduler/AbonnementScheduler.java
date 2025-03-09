package tn.esprit.projet_pi.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.projet_pi.Service.EmailAbonnementService;
import tn.esprit.projet_pi.entity.Abonnement;
import tn.esprit.projet_pi.entity.AbonnementStatus;
import tn.esprit.projet_pi.Repository.AbonnementRepository;

import java.time.LocalDate;
import java.util.List;

@Component
public class AbonnementScheduler {

    private final AbonnementRepository abonnementRepository;
    private final EmailAbonnementService emailAbonnementService;  // Inject your email service here

    public AbonnementScheduler(AbonnementRepository abonnementRepository, EmailAbonnementService emailAbonnementService) {
        this.abonnementRepository = abonnementRepository;
        this.emailAbonnementService = emailAbonnementService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // This runs every day at midnight
    public void updateAbonnementStatus() {
        List<Abonnement> abonnements = abonnementRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (Abonnement abonnement : abonnements) {
            // Check if abonnement is near expiration and has auto-renewal disabled
            if (abonnement.getDateFin().isBefore(currentDate.plusDays(5)) && abonnement.getDateFin().isAfter(currentDate) && !abonnement.getRenouvellementAutomatique()) {
                // Send expiration email
                emailAbonnementService.sendExpirationEmail(abonnement);
            }

            // Check if abonnement has expired and update status
            if (abonnement.getDateFin().isBefore(currentDate) && abonnement.getAbonnementStatus() != AbonnementStatus.EXPIRED) {
                abonnement.setAbonnementStatus(AbonnementStatus.EXPIRED);
                abonnementRepository.save(abonnement);
            }
        }
    }
}
