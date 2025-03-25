package tn.esprit.projet_pi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.projet_pi.Repository.PlatRepository;
import tn.esprit.projet_pi.Repository.ProduitRepository;
import tn.esprit.projet_pi.Repository.UserRepo;
import tn.esprit.projet_pi.entity.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plats")
@CrossOrigin("*")
public class PlatController {
    @Autowired
    private PlatRepository platRepository;
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private ProduitRepository produitRepository;

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

        plat.setAddedBy(user);
        List<Produit> produits = plat.getProduits();
        if (produits == null || produits.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le plat doit avoir au moins un produit.");
        }

        // Fetch the corresponding Produits from the repository
        List<Produit> allProduits = new ArrayList<>();
        List<String> stockErrors = new ArrayList<>();

        for (Produit produitInPlat : produits) {
            Produit produit = produitRepository.findById(produitInPlat.getProduitID())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

            // Check stock for each produit
            if (produit.getQuantite() <= 0) {
                stockErrors.add("Stock insuffisant pour le produit: " + produit.getNomProduit());
            } else {
                // Reduce stock
                produit.setQuantite(produit.getQuantite() - 1);
                produitRepository.save(produit);
                allProduits.add(produit);
            }
        }

        // If there are stock errors, return them
        if (!stockErrors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(stockErrors);
        }

        // Assign the valid list of Produits to the Plat
        plat.setProduits(allProduits);

        // Save the Plat
        Plat savedPlat = platRepository.save(plat);
        return ResponseEntity.ok(savedPlat);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlat(@PathVariable Long id, @RequestBody Plat platDetails, @RequestParam(required = false) Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le paramètre userId est requis");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getRole().equals(Role.Staff)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
        }

        Optional<Plat> optionalPlat = platRepository.findById(id);
        if (optionalPlat.isPresent()) {
            Plat plat = optionalPlat.get();
            plat.setNom(platDetails.getNom());
            plat.setDescription(platDetails.getDescription());
            plat.setCategorie(platDetails.getCategorie());
            return ResponseEntity.ok(platRepository.save(plat));
        } else {
            return ResponseEntity.notFound().build(); // Retourne un 404 si le plat n'existe pas
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


}
