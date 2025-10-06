package tn.esprit.tpprojet.Services;

import org.springframework.cglib.core.Local;
import org.springframework.data.repository.query.Param;
import tn.esprit.tpprojet.Entities.Entreprise;
import tn.esprit.tpprojet.Entities.Equipe;
import tn.esprit.tpprojet.Entities.Projet;

import java.time.LocalDate;
import java.util.List;

public interface IProjetServices {


   List<Entreprise> findByAdresse();
   List<Entreprise> searchByNomOrderByAdresse(String nom);

   boolean existsEquipe(String nomEquipe);

   List<Equipe> afficherSelonEntreprises(String nom);

   //List<Equipe> afficherSelonSpecialiteTriesEntreprise(String specialite);

   List<Projet> afficherProjets(String nom, String description);

   //void supprimerProjets(List<String> listeTechnologies);

   long calculerProjets(LocalDate date1, LocalDate date2);


   public  Projet addProjetAndDetailProjetAndAssign(Projet projet);

   public void assignProjetDetailToProjet(long idProjet, long idProjetDetail);

   public void assignProjetToEquipe(long idEquipe, long idProjet);

   public Projet addProjetAndAssign (Projet projet , long IdProjetDetail);

   public void unassignProjetDetailFromProjet(long idProjet);


   public void unassignProjetToEquipe(long idEquipe , long idProjet);


   public List<Entreprise> afficherEntreprisesSelonAdr(String adresse);


   List<Entreprise> afficherEntreprisesSelonSpecialiteEquipe(String specialite);









}
