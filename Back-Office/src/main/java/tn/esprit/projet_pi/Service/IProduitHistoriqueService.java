package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.Produit;
import tn.esprit.projet_pi.entity.TypeTransaction;

public interface IProduitHistoriqueService {

    public void createHistory(Produit produit, TypeTransaction typeTransaction, int quantite);
}
