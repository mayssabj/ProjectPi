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
