package tn.esprit.projet_pi.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "regime_alimentaire_type")
    private RegimeAlimentaireType regime;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    // New field to track if a doctor has validated the menu
    private Boolean isValidated = false;

    // New field to store total calories
    private Integer totalCalories = 0;

    // New field to reference the doctor who validated the menu
    @ManyToOne
    @JoinColumn(name = "validated_by")
    private User validatedBy;

    @ManyToMany
    @JoinTable(
            name = "menu_plats",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "plats_id")
    )
    private List<Plat> plats;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public RegimeAlimentaireType getRegime() {
        return regime;
    }

    public void setRegime(RegimeAlimentaireType regime) {
        this.regime = regime;
    }

    public List<Plat> getPlats() {
        return plats;
    }

    public void setPlats(List<Plat> plats) {
        this.plats = plats;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(Boolean isValidated) {
        this.isValidated = isValidated;
    }

    public Integer getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(Integer totalCalories) {
        this.totalCalories = totalCalories;
    }

    public User getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(User validatedBy) {
        this.validatedBy = validatedBy;
    }

    // Method to calculate total calories from plats
    public void calculateTotalCalories() {
        if (plats != null && !plats.isEmpty()) {
            this.totalCalories = plats.stream()
                    .filter(plat -> plat.getCalories() != null)
                    .mapToInt(Plat::getCalories)
                    .sum();
        } else {
            this.totalCalories = 0;
        }
    }
}