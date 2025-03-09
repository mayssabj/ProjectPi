package tn.esprit.projet_pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
public class Abonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAbonnement;

    @Enumerated(EnumType.STRING)
    private TypeAbonnement typeAbonnement;

    @Enumerated(EnumType.STRING)
    private AbonnementStatus abonnementStatus;
    private Boolean renouvellementAutomatique;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double cout;
    private Boolean enPeriodeEssai;
    private LocalDate dateFinEssai;

    @OneToOne(mappedBy = "abonnement")
    @ToString.Exclude
    @JsonIgnore
    private User user;

    public Long getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(Long idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public TypeAbonnement getTypeAbonnement() {
        return typeAbonnement;
    }

    public void setTypeAbonnement(TypeAbonnement typeAbonnement) {
        this.typeAbonnement = typeAbonnement;
    }

    public AbonnementStatus getAbonnementStatus() {
        return abonnementStatus;
    }

    public void setAbonnementStatus(AbonnementStatus abonnementStatus) {
        this.abonnementStatus = abonnementStatus;
    }

    public Boolean getRenouvellementAutomatique() {
        return renouvellementAutomatique;
    }

    public void setRenouvellementAutomatique(Boolean renouvellementAutomatique) {
        this.renouvellementAutomatique = renouvellementAutomatique;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Double getCout() {
        return cout;
    }

    public void setCout(Double cout) {
        this.cout = cout;
    }

    public Boolean getEnPeriodeEssai() {
        return enPeriodeEssai;
    }

    public void setEnPeriodeEssai(Boolean enPeriodeEssai) {
        this.enPeriodeEssai = enPeriodeEssai;
    }

    public LocalDate getDateFinEssai() {
        return dateFinEssai;
    }

    public void setDateFinEssai(LocalDate dateFinEssai) {
        this.dateFinEssai = dateFinEssai;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
