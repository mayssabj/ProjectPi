package com.example.forum.Controller;

import com.example.forum.Service.EngagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private final JavaMailSender mailSender;
    private static final Logger log = LoggerFactory.getLogger(EngagementService.class);

    public TestController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @GetMapping("/send-test-email")
    public ResponseEntity<String> sendTestEmail(@RequestParam String to) {
        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("your@gmail.com");
            message.setTo(to);
            message.setSubject("Test Email from Spring");
            message.setText("This is a test email body");

            mailSender.send(message);
            log.info("Email sent successfully to {}", to);
            return ResponseEntity.ok("Email sent to " + to);
        } catch (MailException e) {
            log.error("Email failed to send: {}", e.getMessage());
            return ResponseEntity.status(500)
                    .body("Failed to send email: " + e.getMessage());
        }
    }
}