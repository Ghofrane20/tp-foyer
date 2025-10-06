package tn.esprit.tpprojet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpprojet.Entities.Equipe;

import java.util.List;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {

    boolean existsByNomEquipeIgnoreCase(String nomEquipe);

    List<Equipe> readByEntreprise_Nom(String nom);

    List<Equipe> findBySpecialiteNot(String specialite);

   //List<Equipe> readBySpecialiteContainingOrderByEntreprise_NomEntrepriseDesc(String specialite);


}
