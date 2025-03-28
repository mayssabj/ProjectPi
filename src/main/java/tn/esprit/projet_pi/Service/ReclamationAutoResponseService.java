package tn.esprit.projet_pi.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Repository.ReclamationRepository;
import tn.esprit.projet_pi.entity.Reclamation;
import tn.esprit.projet_pi.entity.ReclamationStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReclamationAutoResponseService {

    @Autowired
    private ReclamationRepository reclamationRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedDelay = 1000)
    public void processUnrespondedReclamations() {
        LocalDateTime recentTime = LocalDateTime.now().minusMinutes(1);
        List<Reclamation> unrespondedReclamations = reclamationRepository
                .findByStatusAndDateCreatedBefore(ReclamationStatus.PENDING, recentTime);


        for (Reclamation reclamation : unrespondedReclamations) {
            // Send an automated response email
            sendAutomatedResponse(reclamation);

            // Update the status to indicate an automated response has been sent
            reclamation.setStatus(ReclamationStatus.IN_PROGRESS);
            reclamationRepository.save(reclamation);
        }
    }

    private void sendAutomatedResponse(Reclamation reclamation) {
        // Ensure user and email are not null
        if (reclamation.getUser() == null || reclamation.getUser().getEmail() == null) {
            return;
        }

        String recipientEmail = reclamation.getUser().getEmail();
        String subject = "Accusé de réception de votre réclamation";
        String message = buildAutomatedResponseMessage(reclamation);

        try {
            emailService.sendReclamationResponse(recipientEmail, subject, message);
        } catch (Exception e) {
            // Log the error or handle it appropriately
            System.err.println("Failed to send automated response for reclamation: " + reclamation.getId());
        }
    }

    private String buildAutomatedResponseMessage(Reclamation reclamation) {
        return String.format(
                "<html>" +
                        "<body>" +
                        "<h2>Accusé de réception de votre réclamation</h2>" +
                        "<p>Bonjour,</p>" +
                        "<p>Nous avons bien reçu votre réclamation concernant : <strong>%s</strong></p>" +
                        "<p>Votre réclamation est actuellement en cours de traitement. Nous vous remercions de votre patience.</p>" +
                        "<p>Détails de la réclamation :</p>" +
                        "<ul>" +
                        "<li><strong>Sujet :</strong> %s</li>" +
                        "<li><strong>Date de création :</strong> %s</li>" +
                        "</ul>" +
                        "<p>Nous vous tiendrons informé de l'évolution de votre dossier.</p>" +
                        "<p>Cordialement,<br>Votre équipe de support</p>" +
                        "</body>" +
                        "</html>",
                reclamation.getDescription(),
                reclamation.getSubject(),
                reclamation.getDateCreated().toString()
        );
    }
}