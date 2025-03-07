package tn.esprit.projet_pi.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

}
