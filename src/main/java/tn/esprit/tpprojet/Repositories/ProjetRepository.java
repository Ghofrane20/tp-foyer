package tn.esprit.tpprojet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.tpprojet.Entities.Projet;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {


    List<Projet> findByNomStartingWithAndProjetDetail_DescriptionContaining(String nom, String description);


    //void removeByProjetDetail_TechnologieIn(List<String> listeTechnologies);

   long countByProjetDetail_DateDebutBetween(LocalDate date1, LocalDate date2);



}
