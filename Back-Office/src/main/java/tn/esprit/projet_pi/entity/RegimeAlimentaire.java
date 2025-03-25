package tn.esprit.projet_pi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
public class RegimeAlimentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RegimeAlimentaireType type;

    @ManyToMany(mappedBy = "regimes")
    @JsonBackReference

    private List<Plat> platsRecommandes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "added_by")
    private User addedBy;

    // Getters and setters


    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegimeAlimentaireType getType() {
        return type;
    }

    public void setType(RegimeAlimentaireType type) {
        this.type = type;
    }

    public List<Plat> getPlatsRecommandes() {
        return platsRecommandes;
    }

    public void setPlatsRecommandes(List<Plat> platsRecommandes) {
        this.platsRecommandes = platsRecommandes;
    }
}