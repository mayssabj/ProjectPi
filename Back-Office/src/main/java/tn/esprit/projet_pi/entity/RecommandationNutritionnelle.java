package tn.esprit.projet_pi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class RecommandationNutritionnelle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommandationId;

    @ManyToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @ManyToOne
    @JoinColumn(name = "medecin_id", nullable = false)
    private User medecin;

    @Column(columnDefinition = "TEXT")
    private String descriptionRecommandation;

    private LocalDate dateRecommandation;

    @Enumerated(EnumType.STRING)
    private StatutRecommandation statut;

    public RecommandationNutritionnelle() {}

    public RecommandationNutritionnelle(Consultation consultation, User medecin, String descriptionRecommandation,
                                        LocalDate dateRecommandation, StatutRecommandation statut) {
        this.consultation = consultation;
        this.medecin = medecin;
        this.descriptionRecommandation = descriptionRecommandation;
        this.dateRecommandation = dateRecommandation;
        this.statut = statut;
    }

    // Getters & Setters
}
