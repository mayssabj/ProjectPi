package tn.esprit.projet_pi.Controller;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Repository.ProduitHistoriqueRepository;
import tn.esprit.projet_pi.Service.IProduitService;
import tn.esprit.projet_pi.Service.ProduitHistoriqueService;
import tn.esprit.projet_pi.entity.Produit;
import tn.esprit.projet_pi.entity.TypeTransaction;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/produit")
public class ProduitController {
    @Autowired
    IProduitService produitService;
    @Autowired
    private ProduitHistoriqueRepository produitHistoriqueRepository;
    @Autowired
    private ProduitHistoriqueService produitHistoriqueService;

    // http://localhost:8081/retrieve-all-produits
    @GetMapping("/retrieve-all-produits")
    public ResponseEntity<List<Produit>> getAllProduits() {
        try {
            List<Produit> listproduits = produitService.getAllProduits();
            return ResponseEntity.ok(listproduits);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add-produit")
    public ResponseEntity<Produit> addProduit(@RequestBody Produit produit) {
        try {
            Produit savedProduit = produitService.addProduit(produit);
            produitHistoriqueService.createHistory(savedProduit, TypeTransaction.CREATED, produit.getQuantite());
            return ResponseEntity.ok(savedProduit);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    //@Transactional
    @DeleteMapping("/remove-produit/{produit-id}")
    public ResponseEntity<String> removeProduit(@PathVariable("produit-id") Integer produitId) {
        try {
            // Fetch the product
            Produit produit = produitService.getProduitById(produitId);

            // Log the deletion in the produit historique (history) before deletion
            produitHistoriqueService.createHistory(produit, TypeTransaction.DELETED, produit.getQuantite());

            // Manually delete the product from the database
            produitService.deleteProduit(produitId);

            return ResponseEntity.ok("Produit deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting produit");
        }
    }


    @PutMapping("/modify-produit/{produit-id}")
    public ResponseEntity<Produit> modifyProduit(@PathVariable("produit-id") Integer produitId, @RequestBody Produit produit) {
        try {
            // Fetch the existing product by ID
            Produit existingProduit = produitService.getProduitById(produitId);
            produitHistoriqueService.createHistory(existingProduit, TypeTransaction.UPDATED, produit.getQuantite());
            if (existingProduit == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // If product not found
            }

            // Set the ID of the updated product to the existing ID
            produit.setProduitID(produitId);

            // Update the produit
            Produit updatedProduit = produitService.modifyProduit(produit);
            return ResponseEntity.ok(updatedProduit);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
}
