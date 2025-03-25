package tn.esprit.projet_pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer produitID;

    private String nomProduit;
    private String description;
    private int quantite;
    private double seuil_alerte;
    private Date date_peremption;


    @OneToMany( mappedBy="produit")
    private List<ProduitHistorique> historiques;

    @JsonIgnore
    @ManyToMany(mappedBy = "produits")

    private List<Plat> plats = new ArrayList<>();
    public List<Plat> getPlats() {
        return plats;
    }

    public void setPlats(List<Plat> plats) {
        this.plats = plats;
    }

    public int getQuantite() {
        return quantite;
    }
    public void setProduitID(Integer produitID) {
        this.produitID = produitID;
    }

    public Integer getProduitID() {
        return produitID;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getSeuil_alerte() {
        return seuil_alerte;
    }

    public void setSeuil_alerte(double seuil_alerte) {
        this.seuil_alerte = seuil_alerte;
    }

    public Date getDate_peremption() {
        return date_peremption;
    }

    public void setDate_peremption(Date date_peremption) {
        this.date_peremption = date_peremption;
    }
}
