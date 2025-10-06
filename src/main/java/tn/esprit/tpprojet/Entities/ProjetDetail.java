package tn.esprit.tpprojet.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjetDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idProjetDetail;
    String description;
    String Technologie;
    Long cout;
    LocalDate dateDebut;


    public Long getCout() {
        return cout;
    }

    public void setCout(Long cout) {
        this.cout = cout;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }



    @OneToOne(mappedBy = "projetDetail")
    @JsonIgnore
            @ToString.Exclude
    Projet projet;

    public long getIdProjetDetail() {
        return idProjetDetail;
    }

    public void setIdProjetDetail(long idProjetDetail) {
        this.idProjetDetail = idProjetDetail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnologie() {
        return Technologie;
    }

    public void setTechnologie(String technologie) {
        Technologie = technologie;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }
}
