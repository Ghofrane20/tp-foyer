package tn.esprit.tpprojet.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpprojet.Entities.Entreprise;
import tn.esprit.tpprojet.Entities.Equipe;
import tn.esprit.tpprojet.Entities.Projet;
import tn.esprit.tpprojet.Services.IProjetServices;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/Projet")
public class ProjetRestController {

    @Autowired
    IProjetServices projetServices;

    @PostMapping("/ajouterProjetEtDetailProjetEtAffecter")
    public Projet ajouterProjetEtDetailProjetEtAffecter(@RequestBody Projet projet) {
        return  projetServices.addProjetAndDetailProjetAndAssign(projet);
    }

    @PutMapping("/affecterProjetDetailAProjet/{idP}/{idPDetail}")
    public  void  affecterProjetADetailProjet(@PathVariable("idP") long idProjet,
                                              @PathVariable("idPDetail")
                                              long idPDetail){
        projetServices.assignProjetDetailToProjet(idProjet,idPDetail);
    }


    @PutMapping("/affecterProjetAEquipe/{idP}/{idEq}")
    public  void  affecterProjetAEquipe(@PathVariable("idP") long idProjet,
                                        @PathVariable("idEq") long idEquipe){
        projetServices.assignProjetToEquipe(idProjet,idEquipe);
    }



    @PostMapping("/ajouterProjetEtAffecter/{idPdetail}")
    public Projet ajouterProjetEtAffecter(@RequestBody Projet projet,@PathVariable("idPdetail") long IdProjetDetail){
        return  projetServices.addProjetAndAssign(projet,IdProjetDetail);
    }


    @GetMapping("/afficherEntreprisesSelonAdr/{adrr}")
    public List<Entreprise> afficherEntreprisesSelonAdr(@PathVariable("adrr") String adresse){
        return projetServices.afficherEntreprisesSelonAdr(adresse);
    }


    @GetMapping("/afficherEntreprisesSelonSpecialiteEquipe/{specialiteEq}")
    public List<Entreprise> afficherEntreprisesSelonSpecialiteEquipe(@PathVariable("specialiteEq") String specialite){
        return projetServices.afficherEntreprisesSelonSpecialiteEquipe(specialite);
    }

    @GetMapping("/getEntreprise")
    public  List<Entreprise> findByAdresse() {
        return projetServices.findByAdresse();
    }

    @GetMapping("/searchEntreprise/{nom}")
    public List<Entreprise> searchByNomOrderByAdresse(String nom){
        return projetServices.searchByNomOrderByAdresse(nom);
    }


    @GetMapping("/searchEquipe/{nomE}")
    public boolean existsEquipe(@PathVariable("nomE") String nomEquipe){
        return projetServices.existsEquipe(nomEquipe);
    }

    @GetMapping("/afficherSelonEntreprises/{nomEntreprise}")
    public List<Equipe> afficherSelonEntreprises(@PathVariable("nomEntreprise") String nom) {
        return projetServices.afficherSelonEntreprises(nom);
    }

    /*
    @GetMapping("/afficherSelonSpecialiteTriesEntreprise/{spec}")
    public List<Equipe> afficherSelonSpecialiteTriesEntreprise(@PathVariable("spec")String specialite){
        return projetServices.afficherSelonSpecialiteTriesEntreprise(specialite);
    }
*/


    @GetMapping("/afficherProjets/{nom}/{description}")
    public List<Projet> afficherProjets(@PathVariable("nom")String nom, @PathVariable("description") String description){
        return projetServices.afficherProjets(nom,description);
    }

    @GetMapping("/calculerProjets/{date1}/{date2}")
    public long calculerProjets(@PathVariable LocalDate date1, @PathVariable LocalDate date2){
        return projetServices.calculerProjets(date1, date2);
    }

}
