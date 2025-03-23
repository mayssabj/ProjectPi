package tn.esprit.projet_pi.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Repository.AbonnementRepository;
import tn.esprit.projet_pi.Repository.TransactionRepository;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.*;
import tn.esprit.projet_pi.interfaces.IAbonnement;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AbonnementService implements IAbonnement {

    final AbonnementRepository abonnementRepository;
    final UserRepo userRepo;
    private final TransactionRepository transactionRepository;
    TransactionService transactionService;
    EmailAbonnementService emailService;

    @Autowired
    public AbonnementService(AbonnementRepository abonnementRepository, UserRepo userRepo, TransactionService transactionService, EmailAbonnementService emailService, TransactionRepository transactionRepository) {
        this.abonnementRepository = abonnementRepository;
        this.userRepo = userRepo;
        this.transactionService = transactionService;
        this.emailService = emailService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public Abonnement createAbonnementByUser(Abonnement abonnement, Long userId) {
        User user = userRepo.findByidUser(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));

        if (user.getAbonnement() != null) {
            throw new RuntimeException("L'utilisateur a déjà un abonnement.");
        }

        // Set the start date
        LocalDate dateDebut = LocalDate.now();
        abonnement.setDateDebut(dateDebut);
        abonnement.setAbonnementStatus(AbonnementStatus.PENDING);
        abonnement.setCout(calculateCout(abonnement.getTypeAbonnement()));
        abonnement.setRemainingDays(abonnement.getRemainingDays());
        abonnement.setConfirmed(abonnement.getConfirmed());
        abonnement.setBlocked(abonnement.getBlocked());


        // Calculate the end date based on the type of subscriptionP
        LocalDate dateFin = calculateDateFin(dateDebut, abonnement.getTypeAbonnement());
        abonnement.setDateFin(dateFin);

        abonnement.setUser(user);
        user.setAbonnement(abonnement);
        Abonnement newAbonnement = abonnementRepository.save(abonnement);
        emailService.sendConfirmationEmail(user, newAbonnement);

        // Create and save the transaction for this abonnement
        Transaction transaction = new Transaction();
        transaction.setAbonnement(newAbonnement);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setMontant(abonnement.getCout());
        transaction.setDateTransaction(LocalDateTime.now().withNano(0));
        transaction.setReferencePaiement("REF-" + newAbonnement.getIdAbonnement());
        transaction.setDetails("Transaction liée à l'abonnement de l'utilisateur" + abonnement.getTypeAbonnement() + " et montant: " + abonnement.getCout());

        transactionService.createTransaction(transaction);
        return newAbonnement;
    }

    @Override
    public void deleteAbonnement(Long userId, Long idAbonnement) {
        checkIfAbonnementBlocked(idAbonnement);
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

        // Check if the abonnement is blocked
        if (existingAbonnement.getBlocked()) {
            throw new RuntimeException("Abonnement is blocked. No further actions can be performed.");
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

        // Save and return the updated abonnement
        return abonnementRepository.save(existingAbonnement);
    }

    @Override
    public Abonnement getAbonnementById(Long userId, Long idAbonnement) {
        checkIfAbonnementBlocked(idAbonnement);
        Abonnement abonnement = abonnementRepository.findById(idAbonnement).orElse(null);
        if (abonnement != null && abonnement.getUser().getId_user().equals(userId)) {
            return abonnement;
        }
        return null;
    }

    private LocalDate calculateDateFin(LocalDate dateDebut, TypeAbonnement typeAbonnement) {
        return switch (typeAbonnement) {
            case MENSUEL -> dateDebut.plusMonths(1);
            case TRIMESTRIEL -> dateDebut.plusMonths(3);
            case SEMESTRIEL -> dateDebut.plusMonths(6);
            case ANNUEL -> dateDebut.plusYears(1);
        };
    }

    private Double calculateCout(TypeAbonnement typeAbonnement) {
        return switch (typeAbonnement) {
            case MENSUEL -> 30.0;       // Monthly subscription
            case TRIMESTRIEL -> 80.0;   // 3 months (save 10Dt)
            case SEMESTRIEL -> 150.0;   // 6 months (save 30Dt)
            case ANNUEL -> 280.0;       // Annual (save 80Dt)
        };
    }

    // Prevent actions if the abonnement is blocked
    public void checkIfAbonnementBlocked(Long abonnementId) {
        Abonnement abonnement = abonnementRepository.findById(abonnementId)
                .orElseThrow(() -> new RuntimeException("Abonnement not found with ID: " + abonnementId));

        if (abonnement.getBlocked()) {
            throw new RuntimeException("Abonnement is blocked. No further actions can be performed.");
        }
    }

    @Transactional
    public Abonnement confirmAbonnement(String confirmationCode) {
        Abonnement abonnement = abonnementRepository.findByConfirmationCode(confirmationCode)
                .orElseThrow(() -> new RuntimeException("Invalid confirmation code"));

        if (abonnement.isCodeExpired()) {
            throw new RuntimeException("Confirmation code has expired");
        }
        // Confirm the abonnement
        abonnement.setConfirmed(true);
        abonnement.setAbonnementStatus(AbonnementStatus.ACTIVE);
        abonnementRepository.save(abonnement);
        sendActivationEmail(abonnement);

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setAbonnement(abonnement);
        transaction.setStatus(TransactionStatus.ACTIVE);
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setMontant(abonnement.getCout());
        transaction.setReferencePaiement("REF-" + abonnement.getIdAbonnement());
        transaction.setDetails("Abonnement confirmé et actif pour l'utilisateur " + abonnement.getUser().getNom());

        // Save using the transaction repository
        transactionRepository.save(transaction);

        return abonnement;
    }


    private void sendActivationEmail(Abonnement abonnement) {
        String userEmail = abonnement.getUser().getEmail();
        String subject = "Your Subscription is Now Active!";
        String text = String.format(
                "Dear %s,\n\n" +
                        "Your subscription has been successfully activated!\n\n" +
                        "Thank you for being a part of our service.\n\n" +
                        "Best regards,\nYour Service Team",
                abonnement.getUser().getNom()
        );

        // Send the email using the EmailAbonnementService
        emailService.sendGenericEmail(userEmail, subject, text);
    }

}
