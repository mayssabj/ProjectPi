package tn.esprit.projet_pi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet_pi.Service.AbonnementService;
import tn.esprit.projet_pi.entity.Abonnement;

@RestController
@RequestMapping("/api/abonnement")
public class AbonnementController {
    final AbonnementService abonnementService;

    @Autowired
    public AbonnementController(AbonnementService abonnementService) {
        this.abonnementService = abonnementService;
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<Abonnement> addAbonnement(@RequestBody Abonnement abonnement, @PathVariable("userId") Long userId) {
        try {
            Abonnement createdAbonnement = abonnementService.createAbonnementByUser(abonnement, userId);
            return new ResponseEntity<>(createdAbonnement, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{userId}/{idAbonnement}")
    public ResponseEntity<Void> deleteAbonnement(@PathVariable Long userId, @PathVariable Long idAbonnement) {
        try {
            abonnementService.deleteAbonnement(userId, idAbonnement);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<Abonnement> updateAbonnement(@PathVariable Long userId, @RequestBody Abonnement abonnement) {
        try {
            Abonnement updatedAbonnement = abonnementService.updateAbonnement(userId, abonnement);
            return new ResponseEntity<>(updatedAbonnement, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/{userId}/{idAbonnement}")
    public ResponseEntity<Abonnement> getAbonnementById(@PathVariable Long userId, @PathVariable Long idAbonnement) {
        Abonnement abonnement = abonnementService.getAbonnementById(userId, idAbonnement);
        if (abonnement != null) {
            return new ResponseEntity<>(abonnement, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
