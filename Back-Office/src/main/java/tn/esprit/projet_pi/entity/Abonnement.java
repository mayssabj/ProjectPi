package tn.esprit.projet_pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
public class Abonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAbonnement;

    @Enumerated(EnumType.STRING)
    private TypeAbonnement typeAbonnement;

    @Enumerated(EnumType.STRING)
    private AbonnementStatus abonnementStatus;

    private Boolean renouvellementAutomatique = false;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double cout;
    private long remainingDays;


    @Column(unique = true)
    private String confirmationCode;

    private LocalDateTime codeExpiration;
    private Boolean isBlocked = false;
    private Boolean isConfirmed = false;

    @OneToMany(mappedBy = "abonnement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;

    @OneToOne(mappedBy = "abonnement")
    @JsonIgnore
    private User user;

    public long getRemainingDays() {
        if (dateFin == null) {
            return 0;
        }
        long days = ChronoUnit.DAYS.between(LocalDate.now(), dateFin);
        return Math.max(days, 0); // Ensures no negative days
    }


    @PrePersist
    private void generateConfirmationCode() {
        this.confirmationCode = generateUniqueCode();
        this.codeExpiration = LocalDateTime.now().plusHours(24);
    }

    private String generateUniqueCode() {
        return "CONF-" +
                LocalDateTime.now().hashCode() +
                "-" +
                (long) (Math.random() * 1000000);
    }

    public boolean isCodeExpired() {
        return LocalDateTime.now().isAfter(codeExpiration);
    }

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

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Double getCout() {
        return cout;
    }

    public void setCout(Double cout) {
        this.cout = cout;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public LocalDateTime getCodeExpiration() {
        return codeExpiration;
    }

    public void setCodeExpiration(LocalDateTime codeExpiration) {
        this.codeExpiration = codeExpiration;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public Boolean getConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        isConfirmed = confirmed;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void setRemainingDays(long remainingDays) {
        this.remainingDays = remainingDays;
    }
}