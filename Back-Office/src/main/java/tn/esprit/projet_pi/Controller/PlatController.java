package tn.esprit.projet_pi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Repository.PlatRepository;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.Service.FileStorageService;
import tn.esprit.projet_pi.entity.CategoriePlat;
import tn.esprit.projet_pi.entity.Plat;
import tn.esprit.projet_pi.entity.Role;
import tn.esprit.projet_pi.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/plats")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

public class PlatController {
    @Autowired
    private PlatRepository platRepository;

    @Autowired
    private UserRepo userRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public List<Plat> getAllPlats() {
        return platRepository.findAll();
    }

    @GetMapping("/categorie/{categorie}")
    public List<Plat> getPlatsByCategorie(@PathVariable CategoriePlat categorie) {
        return platRepository.findByCategorie(categorie);
    }

    @PostMapping("/addplat")
    public ResponseEntity<?> addPlat(@RequestBody Plat plat, @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre userId est requis");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }

        // Vérification des calories
        if (plat.getCalories() == null || plat.getCalories() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Les calories doivent être spécifiées et positives");
        }

        plat.setAddedBy(user);
        Plat savedPlat = platRepository.save(plat);
        return ResponseEntity.ok(savedPlat);
    }
    @GetMapping("/{id}")
    public Optional<Plat> getPlatsById(@PathVariable Long id) {
        return platRepository.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlat(@PathVariable Long id, @RequestBody Plat platDetails,
                                        @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre userId est requis");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }

        // Vérification des calories
        if (platDetails.getCalories() != null && platDetails.getCalories() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Les calories doivent être positives");
        }

        Optional<Plat> optionalPlat = platRepository.findById(id);
        if (optionalPlat.isPresent()) {
            Plat plat = optionalPlat.get();
            plat.setNom(platDetails.getNom());
            plat.setDescription(platDetails.getDescription());
            plat.setCategorie(platDetails.getCategorie());

            // Mise à jour des calories
            if (platDetails.getCalories() != null) {
                plat.setCalories(platDetails.getCalories());
            }

            return ResponseEntity.ok(platRepository.save(plat));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlat(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre userId est requis");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }

        if (platRepository.existsById(id)) {
            platRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Modified uploadImage method to accept image URL instead of file upload
    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<?> uploadImage(@PathVariable Long id,
                                         @RequestBody Map<String, String> payload,
                                         @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre userId est requis");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }

        Optional<Plat> optionalPlat = platRepository.findById(id);
        if (!optionalPlat.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        String imageUrl = payload.get("image");
        if (imageUrl == null || imageUrl.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'URL de l'image est requise");
        }

        // Update the plat with the image URL
        Plat plat = optionalPlat.get();
        plat.setImagePath(imageUrl);
        platRepository.save(plat);

        return ResponseEntity.ok().body("Image URL ajoutée avec succès");
    }

    // Modified addplatWithImage method to accept JSON instead of multipart form
    @PostMapping(value = "/addplatWithImage", consumes = {"application/json"})
    public ResponseEntity<?> addPlatWithImage(@RequestBody Plat plat,
                                              @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre userId est requis");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }

        // Vérification des calories
        if (plat.getCalories() == null || plat.getCalories() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Les calories doivent être spécifiées et positives");
        }

        // Set the user who added the plat
        plat.setAddedBy(user);

        Plat savedPlat = platRepository.save(plat);
        return ResponseEntity.ok(savedPlat);
    }

    // Modified updateWithImage method to accept JSON body instead of multipart form
    @PutMapping("/{id}/updateWithImage")
    public ResponseEntity<?> updatePlatWithImage(@PathVariable Long id,
                                                 @RequestBody Plat platDetails,
                                                 @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre userId est requis");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }

        // Vérification des calories
        if (platDetails.getCalories() != null && platDetails.getCalories() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Les calories doivent être positives");
        }

        Optional<Plat> optionalPlat = platRepository.findById(id);
        if (optionalPlat.isPresent()) {
            Plat plat = optionalPlat.get();

            if (platDetails.getNom() != null) plat.setNom(platDetails.getNom());
            if (platDetails.getDescription() != null) plat.setDescription(platDetails.getDescription());
            if (platDetails.getCategorie() != null) plat.setCategorie(platDetails.getCategorie());
            if (platDetails.getCalories() != null) plat.setCalories(platDetails.getCalories());
            if (platDetails.getImagePath() != null) plat.setImagePath(platDetails.getImagePath());

            return ResponseEntity.ok(platRepository.save(plat));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
