package tn.esprit.projet_pi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ValidationMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long validationId;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "medecin_id", nullable = false)
    private User medecin;

    private LocalDate dateValidation;

    @Enumerated(EnumType.STRING)
    private DecisionValidation decision;

    private String motifRejet;

    private String commentaire;

    private Integer scoreQualite;

    public ValidationMenu() {}

    public ValidationMenu(Menu menu, User medecin, LocalDate dateValidation,
                          DecisionValidation decision, String motifRejet, String commentaire, Integer scoreQualite) {
        this.menu = menu;
        this.medecin = medecin;
        this.dateValidation = dateValidation;
        this.decision = decision;
        this.motifRejet = motifRejet;
        this.commentaire = commentaire;
        this.scoreQualite = scoreQualite;
    }
    // Getters & Setters
}
