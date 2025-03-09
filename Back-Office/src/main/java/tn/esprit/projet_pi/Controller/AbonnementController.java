package tn.esprit.projet_pi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/add/{user-id}")
    public Abonnement addAbonnement(@RequestBody Abonnement a,@PathVariable("user-id") Long userId){
        Abonnement abonnement = abonnementService.createAbonnementByUser(a,userId);
        return abonnement;
    }

    @DeleteMapping("/delete/{userId}/{idAbonnement}")
    public void deleteAbonnement(@PathVariable Long userId, @PathVariable Long idAbonnement){
        abonnementService.deleteAbonnement(userId, idAbonnement);
    }

    @PutMapping("/update/{userId}")
    public Abonnement updateAbonnement(@PathVariable Long userId, @RequestBody Abonnement abonnement){
        return abonnementService.updateAbonnement(userId, abonnement);
    }

    @GetMapping("/get/{userId}/{idAbonnement}")
    public Abonnement getAbonnementById(@PathVariable Long userId, @PathVariable Long idAbonnement){
        return abonnementService.getAbonnementById(userId, idAbonnement);
    }

}
