package tn.esprit.projet_pi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommandationNutritionnelle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private User medecin;

    private LocalDate dateRecommandation;

    @Lob
    private String descriptionRecommandation;

    @Enumerated(EnumType.STRING)
    private StatutRecommandation statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public User getMedecin() {
        return medecin;
    }

    public void setMedecin(User medecin) {
        this.medecin = medecin;
    }

    public LocalDate getDateRecommandation() {
        return dateRecommandation;
    }

    public void setDateRecommandation(LocalDate dateRecommandation) {
        this.dateRecommandation = dateRecommandation;
    }

    public String getDescriptionRecommandation() {
        return descriptionRecommandation;
    }

    public void setDescriptionRecommandation(String descriptionRecommandation) {
        this.descriptionRecommandation = descriptionRecommandation;
    }

    public StatutRecommandation getStatut() {
        return statut;
    }

    public void setStatut(StatutRecommandation statut) {
        this.statut = statut;
    }


}
