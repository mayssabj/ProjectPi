package tn.esprit.projet_pi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consultationId;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "medecin_id", nullable = false)
    private User medecin;

    private LocalDateTime dateConsultation;

    @Enumerated(EnumType.STRING)
    private StatutConsultation statut;

    @Column(columnDefinition = "TEXT")
    private String compteRendu;

    public Consultation() {}

    public Consultation(User user, User medecin, LocalDateTime dateConsultation, StatutConsultation statut, String compteRendu) {
        this.user = user;
        this.medecin = medecin;
        this.dateConsultation = dateConsultation;
        this.statut = statut;
        this.compteRendu = compteRendu;
    }

    // Getters & Setters
}
