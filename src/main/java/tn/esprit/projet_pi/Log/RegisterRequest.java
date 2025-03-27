package tn.esprit.projet_pi.Log;

import lombok.Getter;
import lombok.Setter;
import tn.esprit.projet_pi.entity.Role;

@Getter
@Setter
public class RegisterRequest {
    public String nom;
    public String email;
    public Role role;
    public int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String mdp;

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getMdp() {
        return mdp;
    }
}
