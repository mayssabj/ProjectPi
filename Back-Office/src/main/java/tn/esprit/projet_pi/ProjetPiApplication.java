package tn.esprit.projet_pi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjetPiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetPiApplication.class, args);


    }

}
