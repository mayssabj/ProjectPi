package tn.esprit.projet_pi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.awt.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "nutritionniste_id")
    private User nutritionniste;

    private LocalDate dateValidation;

    @Enumerated(EnumType.STRING)
    private DecisionMenu decision;

    private String motifRejet;
    private String commentaire;
    private Integer scoreQualite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public User getNutritionniste() {
        return nutritionniste;
    }

    public void setNutritionniste(User nutritionniste) {
        this.nutritionniste = nutritionniste;
    }

    public LocalDate getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public DecisionMenu getDecision() {
        return decision;
    }

    public void setDecision(DecisionMenu decision) {
        this.decision = decision;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

    public void setMotifRejet(String motifRejet) {
        this.motifRejet = motifRejet;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Integer getScoreQualite() {
        return scoreQualite;
    }

    public void setScoreQualite(Integer scoreQualite) {
        this.scoreQualite = scoreQualite;
    }
}
