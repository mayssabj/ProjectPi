package tn.esprit.projet_pi.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.projet_pi.Repository.ProduitRepository;
import tn.esprit.projet_pi.entity.Produit;

import java.util.List;
@Service
public class ProduitService implements IProduitService {
    @Autowired
    private  ProduitRepository produitRepository;
    @Override
    public Produit addProduit(Produit produit) {
        return produitRepository.save(produit);
    }
    @Override
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }
    @Override
    public  Produit modifyProduit(Produit Produit) {
        return produitRepository.save(Produit);
    }
    @Override
    public Produit deleteProduit(Integer id) {
        Produit produit = produitRepository.findById(id).orElseThrow(() -> new RuntimeException("Produit not found"));
        produitRepository.deleteById(id);
        return produit;
    }

    @Override
    public Produit getProduitById(Integer produitId) {
        return produitRepository.findById(produitId).orElse(null); // Returns the product if found, else null
    }
}
