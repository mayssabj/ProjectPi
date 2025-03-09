package tn.esprit.projet_pi.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.entity.Abonnement;

@Service
public class EmailAbonnementService {

    private final JavaMailSender javaMailSender;

    public EmailAbonnementService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendExpirationEmail(Abonnement abonnement) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(abonnement.getUser().getEmail());
        message.setSubject("Abonnement Expiry Notice");
        message.setText("Dear " + abonnement.getUser().getNom() + ",\n\n" +
                "Your abonnement is about to expire on " + abonnement.getDateFin() +
                ".\nSince you have disabled automatic renewal, please renew it manually if you wish to continue using our services.\n\n" +
                "Best regards,\nYour Service Team");

        javaMailSender.send(message);
    }
}
