package tn.esprit.projet_pi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.entity.Abonnement;
import tn.esprit.projet_pi.entity.User;

@Service
public class EmailAbonnementService {

    private final JavaMailSender javaMailSender;
    public EmailAbonnementService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendConfirmationEmail(User user, Abonnement abonnement) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Confirm Your Subscription");

        String text = String.format(
                "Dear %s,\n\n" +
                        "Thank you for subscribing to our service!\n\n" +
                        "Your confirmation code is: **%s**\n" +
                        "This code will expire on: %s\n\n" +
                        "To confirm your subscription, please use this code during the payment process.\n\n" +
                        "Best regards,\nYour Service Team",
                user.getNom(),
                abonnement.getConfirmationCode(),
                abonnement.getCodeExpiration().toString()
        );

        message.setText(text);
        javaMailSender.send(message);
    }

    @Async
    public void sendExpirationEmail(Abonnement abonnement) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(abonnement.getUser().getEmail());
        message.setSubject("Abonnement Expiry Notice");

        String text = String.format(
                "Dear %s,\n\n" +
                        "Your abonnement is about to expire on %s.\n\n" +
                        "Since you have disabled automatic renewal, please renew it manually if you wish to continue using our services.\n\n" +
                        "Best regards,\nYour Service Team",
                abonnement.getUser().getNom(),
                abonnement.getDateFin().toString()
        );

        message.setText(text);
        javaMailSender.send(message);
    }

    @Async
    public void sendGenericEmail(String email, String subject, String messageContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(messageContent);
        javaMailSender.send(message);
    }
}