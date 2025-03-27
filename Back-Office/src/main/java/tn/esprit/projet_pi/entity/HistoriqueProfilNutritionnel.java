package tn.esprit.projet_pi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueProfilNutritionnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profil_id")
    private ProfilNutritionnel profil;

    private LocalDateTime dateEnregistrement;
    private Double poids;

    @Enumerated(EnumType.STRING)
    private NiveauActivite niveauActivite;

    private String objectif;
    private Double imc;
    private Integer besoinCalorique;


    private String commentaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfilNutritionnel getProfil() {
        return profil;
    }

    public void setProfil(ProfilNutritionnel profil) {
        this.profil = profil;
    }

    public LocalDateTime getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(LocalDateTime dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }

    public NiveauActivite getNiveauActivite() {
        return niveauActivite;
    }

    public void setNiveauActivite(NiveauActivite niveauActivite) {
        this.niveauActivite = niveauActivite;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public Double getImc() {
        return imc;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public Integer getBesoinCalorique() {
        return besoinCalorique;
    }

    public void setBesoinCalorique(Integer besoinCalorique) {
        this.besoinCalorique = besoinCalorique;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
