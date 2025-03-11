package tn.esprit.projet_pi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


}
