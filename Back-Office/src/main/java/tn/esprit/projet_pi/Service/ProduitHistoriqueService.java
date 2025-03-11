package tn.esprit.projet_pi.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Repository.ProduitHistoriqueRepository;
import tn.esprit.projet_pi.entity.Produit;
import tn.esprit.projet_pi.entity.ProduitHistorique;
import tn.esprit.projet_pi.entity.TypeTransaction;

import java.util.Date;

@Service
public class ProduitHistoriqueService  {

    @Autowired
    private ProduitHistoriqueRepository produitHistoriqueRepository;
    // Method to create history

    public void createHistory(Produit produit, TypeTransaction typeTransaction, int quantite) {
        ProduitHistorique historique = new ProduitHistorique();
        historique.setProduit(produit);
        historique.setType(typeTransaction);
        historique.setQuantite(quantite);
        historique.setDate(new Date());  // Set the current date and time

        produitHistoriqueRepository.save(historique);
    }

}
