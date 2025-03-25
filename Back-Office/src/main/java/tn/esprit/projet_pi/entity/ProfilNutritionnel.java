package tn.esprit.projet_pi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class ProfilNutritionnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profilId;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;

    @NotBlank
    private String objectif;

    @Enumerated(EnumType.STRING)
    private NiveauActivite niveauActivite;

    @Positive
    private Double poidsActuel;

    @Positive
    private Double taille;

    @Transient
    public Double getImc() {
        return (taille != null && taille > 0) ? poidsActuel / (taille * taille) : null;
    }

    @Enumerated(EnumType.STRING)
    private RegimeAlimentaireType regime;

    private String allergies;

    private Integer besoinCalorique;

    private String derniereEvolution;

    private LocalDate derniereMiseAJour;

    public ProfilNutritionnel() {}

    public ProfilNutritionnel(User user, String objectif, NiveauActivite niveauActivite, Double poidsActuel,
                              Double taille, RegimeAlimentaireType regime, String allergies, Integer besoinCalorique,
                              String derniereEvolution, LocalDate derniereMiseAJour) {
        this.user = user;
        this.objectif = objectif;
        this.niveauActivite = niveauActivite;
        this.poidsActuel = poidsActuel;
        this.taille = taille;
        this.regime = regime;
        this.allergies = allergies;
        this.besoinCalorique = besoinCalorique;
        this.derniereEvolution = derniereEvolution;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    // Getters & Setters
    // (tu peux copier les tiens existants ici)
}
