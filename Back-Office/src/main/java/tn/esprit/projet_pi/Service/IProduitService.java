package tn.esprit.projet_pi.Service;

import tn.esprit.projet_pi.entity.Produit;
import java.util.List;

public interface IProduitService {

    public Produit addProduit(Produit produit);
    public List<Produit> getAllProduits();
    public Produit modifyProduit(Produit Produit);
    public Produit deleteProduit(Integer id);
    public Produit getProduitById(Integer produitId);

}
