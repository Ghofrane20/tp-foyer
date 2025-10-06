package tn.esprit.tpprojet.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idProjet;
    String nom;
    @OneToOne(cascade = CascadeType.ALL)
    ProjetDetail projetDetail;

    public long getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(long idProjet) {
        this.idProjet = idProjet;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ProjetDetail getProjetDetail() {
        return projetDetail;
    }

    public void setProjetDetail(ProjetDetail projetDetail) {
        this.projetDetail = projetDetail;
    }
}
