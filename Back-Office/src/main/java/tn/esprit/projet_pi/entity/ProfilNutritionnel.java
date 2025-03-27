package tn.esprit.projet_pi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfilNutritionnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Sexe sexe;

    private Double poidsActuel;
    private Double taille;

    @Enumerated(EnumType.STRING)
    private NiveauActivite niveauActivite;

    private String objectif;
    private String allergies;

    @Enumerated(EnumType.STRING)
    private RegimeAlimentaireType regimeAlimentaire;

    private Double imc;
    private Integer besoinCalorique;

    private String derniereEvolution;
    private LocalDateTime derniereMiseAJour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getPoidsActuel() {
        return poidsActuel;
    }

    public void setPoidsActuel(Double poidsActuel) {
        this.poidsActuel = poidsActuel;
    }

    public Double getTaille() {
        return taille;
    }

    public void setTaille(Double taille) {
        this.taille = taille;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public NiveauActivite getNiveauActivite() {
        return niveauActivite;
    }

    public void setNiveauActivite(NiveauActivite niveauActivite) {
        this.niveauActivite = niveauActivite;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public RegimeAlimentaireType getRegimeAlimentaire() {
        return regimeAlimentaire;
    }

    public void setRegimeAlimentaire(RegimeAlimentaireType regimeAlimentaire) {
        this.regimeAlimentaire = regimeAlimentaire;
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

    public String getDerniereEvolution() {
        return derniereEvolution;
    }

    public void setDerniereEvolution(String derniereEvolution) {
        this.derniereEvolution = derniereEvolution;
    }

    public LocalDateTime getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(LocalDateTime derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }
}
