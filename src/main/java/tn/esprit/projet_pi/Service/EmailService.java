package tn.esprit.projet_pi.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.entity.Reclamation;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String toEmail, String token) {
        String resetUrl = "http://localhost:8081/api/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Réinitialisation de votre mot de passe");
        message.setText("Cliquez sur le lien suivant pour réinitialiser votre mot de passe : " + resetUrl);

        mailSender.send(message);
        System.out.println("Email envoyé à : " + toEmail);
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String verificationUrl = "http://localhost:8081/api/auth/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Vérification de votre e-mail");
        message.setText("Cliquez sur le lien suivant pour vérifier votre e-mail : " + verificationUrl);

        mailSender.send(message);
        System.out.println("Email de vérification envoyé à : " + toEmail);
    }

    public void sendReclamationResponse(String recipientEmail, String subject, String message) {
        if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient email cannot be null or empty");
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(recipientEmail); // This is where the error occurs
            helper.setSubject(subject);
            helper.setText(message, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendReclamationResponse(Reclamation savedReclamation) {
        if (savedReclamation.getUser() == null ||
                savedReclamation.getUser().getEmail() == null) {
            return; // Early return if no user or email
        }

        String recipientEmail = savedReclamation.getUser().getEmail();
        String subject = "Réclamation Reçue - Accusé de Réception";
        String message = String.format(
                "Votre réclamation a été reçue.\n" +
                        "Sujet: %s\n" +
                        "Description: %s\n" +
                        "Date: %s",
                savedReclamation.getSubject(),
                savedReclamation.getDescription(),
                savedReclamation.getDateCreated()
        );

        sendReclamationResponse(recipientEmail, subject, message);
    }



    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


}
