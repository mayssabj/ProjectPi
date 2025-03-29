package tn.esprit.projet_pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;
    private long numero;
    private String access;
    private String nom;
    private int age;
    private String email;
    private String mdp;
    private String Link_Image;
    private String adresse;
    private String telephone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean is_verified;
    @Column(unique = true)
    private String resetToken;
    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private List<Menu> menus;

    @OneToMany(mappedBy = "addedBy")
    @JsonIgnore
    private List<Plat> plats;

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Long getId_user() {
        return idUser;
    }

    public void setId_user(Long id_user) {
        this.idUser = id_user;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getLink_Image() {
        return Link_Image;
    }

    public void setLink_Image(String link_Image) {
        Link_Image = link_Image;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(Boolean is_verified) {
        this.is_verified = is_verified;
    }


    public User() {}
}

