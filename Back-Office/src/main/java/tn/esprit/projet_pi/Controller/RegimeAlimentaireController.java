package tn.esprit.projet_pi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Repository.PlatRepository;
import tn.esprit.projet_pi.Repository.RegimeAlimentaireRepository;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/regimes")
@CrossOrigin(origins = "http://localhost:4200")
public class RegimeAlimentaireController {

    @Autowired
    private RegimeAlimentaireRepository regimeAlimentaireRepository;

    @Autowired
    private UserRepo userRepository;
    @Autowired
    private PlatRepository platRepository;


  @GetMapping
   public List<RegimeAlimentaire> getAllRegimes() {
       return (List<RegimeAlimentaire>) regimeAlimentaireRepository.findAll();
    }
//@GetMapping
//public List<RegimeAlimentaire> getAllRegimes() {
//    return regimeAlimentaireRepository.findAllWithPlats();
//}



    @GetMapping("/{type}")
    public RegimeAlimentaire getRegimeByType(@PathVariable RegimeAlimentaireType type) {
        Optional<RegimeAlimentaire> regime = regimeAlimentaireRepository.findFirstByType(type);
        return regime.orElseThrow(() -> new RuntimeException("Régime alimentaire non trouvé : " + type));
    }

    @PostMapping("/aadregime")
    public ResponseEntity<?> addRegime(@RequestBody RegimeAlimentaire regime, @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre userId est requis");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }

        regime.setAddedBy(user);
        return ResponseEntity.ok(regimeAlimentaireRepository.save(regime));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRegime(@PathVariable Long id, @RequestBody RegimeAlimentaire regimeDetails, @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre userId est requis");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }

        Optional<RegimeAlimentaire> optionalRegime = regimeAlimentaireRepository.findById(id);
        if (optionalRegime.isPresent()) {
            RegimeAlimentaire regime = optionalRegime.get();
            regime.setType(regimeDetails.getType());
            return ResponseEntity.ok(regimeAlimentaireRepository.save(regime));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegime(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre userId est requis");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }

        if (regimeAlimentaireRepository.existsById(id)) {
            regimeAlimentaireRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }
    @PutMapping("/{id}/addPlats")
    public ResponseEntity<?> assignPlatsToRegime(@PathVariable Long id,
                                                 @RequestBody List<Long> platIds,
                                                 @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre userId est requis");
        }

        User user = userRepository.findByidUser(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }

        RegimeAlimentaire regime = regimeAlimentaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Régime alimentaire non trouvé"));

        List<Plat> plats = platRepository.findAllById(platIds);
        if (plats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Aucun plat valide trouvé");
        }

        // Ajouter la relation dans les deux sens
        for (Plat plat : plats) {
            if (!regime.getPlatsRecommandes().contains(plat)) {
                regime.getPlatsRecommandes().add(plat);
                plat.getRegimes().add(regime);
            }
        }

        // Sauvegarde des modifications
        regimeAlimentaireRepository.save(regime);
        platRepository.saveAll(plats);  // 🔥 Ajout de cette ligne pour s'assurer que la relation est bien sauvegardée

        return ResponseEntity.ok("Plats ajoutés avec succès !");
    }




}