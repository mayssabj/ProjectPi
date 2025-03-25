package tn.esprit.projet_pi.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProduitHistorique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historiqueID;

    public Integer getHistoriqueID() {
        return historiqueID;
    }

    public void setHistoriqueID(Integer historiqueID) {
        this.historiqueID = historiqueID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public TypeTransaction getType() {
        return type;
    }

    public void setType(TypeTransaction type) {
        this.type = type;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    // Relation avec Produit (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    // Relation avec Type de Transaction (enum)
    @Enumerated(EnumType.STRING)
    private TypeTransaction type;

    private int quantite;
    private Date date;
}
