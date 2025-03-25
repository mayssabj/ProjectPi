package tn.esprit.projet_pi.Service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    /*private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetEmail(String to, String token) {
        String resetLink = "http://localhost:8081/api/auth/reset-password?token=" + token;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Réinitialisation du mot de passe");
            helper.setText("Cliquez sur le lien pour réinitialiser votre mot de passe : " + resetLink);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }*/
}
