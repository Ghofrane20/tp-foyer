package tn.esprit.tpprojet.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tpprojet.Entities.Entreprise;
import tn.esprit.tpprojet.Entities.Equipe;
import tn.esprit.tpprojet.Entities.Projet;
import tn.esprit.tpprojet.Entities.ProjetDetail;
import tn.esprit.tpprojet.Repositories.EntrepriseRepository;
import tn.esprit.tpprojet.Repositories.EquipeRepository;
import tn.esprit.tpprojet.Repositories.ProjetDetailRepository;
import tn.esprit.tpprojet.Repositories.ProjetRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class IProjetServicesImpl implements IProjetServices {

    @Autowired
    ProjetRepository projetRepository;
    @Autowired
    ProjetDetailRepository projetDetailRepository;
    @Autowired
    EquipeRepository equipeRepository;
    @Autowired
    EntrepriseRepository entrepriseRepository;



    @Override
    public List<Entreprise> findByAdresse() {
        return entrepriseRepository.findByAdresseIsNotNull();
    }

    @Override
    public List<Entreprise> searchByNomOrderByAdresse(String nom) {
        return entrepriseRepository.searchByNomContainingOrderByAdresseAsc(nom);
    }

    @Override
    public boolean existsEquipe(String nomEquipe) {
        return equipeRepository.existsByNomEquipeIgnoreCase(nomEquipe);
    }

    @Override
    public List<Equipe> afficherSelonEntreprises(String nom) {
        return equipeRepository.readByEntreprise_Nom(nom);
    }

    /*
    @Override
    public List<Equipe> afficherSelonSpecialiteTriesEntreprise(String specialite) {
        return equipeRepository.readBySpecialiteContainingOrderByEntreprise_NomEntrepriseDesc(specialite);
    }
*/
    @Override
    public List<Projet> afficherProjets(String nom, String description) {
        return projetRepository.findByNomStartingWithAndProjetDetail_DescriptionContaining(nom, description);
    }

    /*
    @Override
    public void supprimerProjets(List<String> listeTechnologies) {
       projetRepository.removeByProjetDetail_TechnologieIn(listeTechnologies);
    }
*/
    @Override
    public long calculerProjets(LocalDate date1, LocalDate date2) {
        return projetRepository.countByProjetDetail_DateDebutBetween(date1, date2);
    }


    @Override
    public Projet addProjetAndDetailProjetAndAssign(Projet projet) {
        return projetRepository.save(projet);
    }

    @Override
    public void assignProjetDetailToProjet(long idProjet, long idProjetDetail) {

        //Récupération des objets de la BD
        Projet projet = projetRepository.findById(idProjet).get();
        ProjetDetail projetDetail = projetDetailRepository.
                findById(idProjetDetail).get();

        //Affecter ProjetDetailToProjet
        projet.setProjetDetail(projetDetail);

        //Sauvgarder Projet
        projetRepository.save(projet);

    }

    @Override
    public void assignProjetToEquipe(long idEquipe, long idProjet) {
        Projet projet = projetRepository.
                findById(idProjet).get();
        Equipe equipe = equipeRepository.
                findById(idEquipe).get();

        equipe.getProjets().add(projet);
        equipeRepository.save(equipe);
    }

    @Override
    public Projet addProjetAndAssign(Projet projet, long IdProjetDetail) {
        ProjetDetail detail = projetDetailRepository.
                findById(IdProjetDetail).get();
        projet.setProjetDetail(detail);
       return projetRepository.save(projet);
    }

    @Override
    public void unassignProjetDetailFromProjet(long idProjet) {
        Projet projet = projetRepository.findById(idProjet).get();
        projet.setProjetDetail(null);
        projetRepository.save(projet);
    }

    @Override
    public void unassignProjetToEquipe(long idEquipe, long idProjet) {
        Projet projet = projetRepository.findById(idProjet).get();
        Equipe equipe = equipeRepository.findById(idEquipe).get();

        equipe.getProjets().remove(projet);
        equipeRepository.save(equipe);
    }

    @Override
    public List<Entreprise> afficherEntreprisesSelonAdr(String adresse) {
        return entrepriseRepository.afficherEntreprises1(adresse);
    }

    @Override
    public List<Entreprise> afficherEntreprisesSelonSpecialiteEquipe(String specialite) {
        return entrepriseRepository.afficherEntreprisesSelonSpecialiteEquipe(specialite);
    }


}
