package tn.esprit.projet_pi.Log;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.projet_pi.entity.User;

import java.util.Date;

    @Service
    public class JwtService {

        private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A713474375367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
        private static final long EXPIRATION_TIME = 86400000; // 1 jour

        // Générer un token JWT
        public static String generateToken(User user) {
            return Jwts.builder()
                    .setSubject(user.getEmail())  // Utilisez l'email comme sujet
                    .claim("nom", user.getNom())  // Ajout du nom
                    .claim("role", user.getRole().name()) // Ajoutez un claim pour le rôle
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .compact();
        }



        // Extraire l'email (ou username) du token
        public String extractUsername(String token) {
            return extractClaims(token).getSubject();
        }

        // Valider le token JWT
        public boolean validateToken(String token, UserDetails userDetails) {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        }

        // Vérifier si le token est expiré
        private boolean isTokenExpired(String token) {
            return extractClaims(token).getExpiration().before(new Date());
        }

        // Extraire les claims du token
        private Claims extractClaims(String token) {
            return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        }
    }
