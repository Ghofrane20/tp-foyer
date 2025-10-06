package tn.esprit.tpprojet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tpprojet.Entities.Entreprise;

import java.util.List;
import java.util.Optional;


public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {


    Entreprise readByNom(String nom);

    Optional<Entreprise> findDistinctByNom(String nom);

    List<Entreprise> findByAdresseIsNotNull();

    List<Entreprise> searchByNomContainingOrderByAdresseAsc(String nom);

    @Query("select e from Entreprise e where e.adresse = :adrr")
    public List<Entreprise>  afficherEntreprises1(@Param("adrr") String adresse);


    @Query("select e from Entreprise e where e.adresse = ?1")
    public List<Entreprise>  afficherEntreprises2(String adresse);

    @Query(value = "select * from  entreprise e where e.adresse = :adrr",
            nativeQuery = true)
    public List<Entreprise>  afficherEntreprises3(@Param("adrr") String adresse);


    @Query("select e from Entreprise e , Equipe eq where e.idEntreprise = eq.entreprise.idEntreprise " +
            "and eq.specialite = :specialite")
    public List<Entreprise>  afficherEntreprisesSelonSpecialiteEquipe
            (@Param("specialite") String specialite);


    @Modifying
    @Query("update Entreprise e set e.adresse= :adrr where e.nom= :nom")
    public void modifierEntreprise(@Param("adrr") String adresse, @Param("nom") String nom);

    @Modifying
    @Query("delete from Entreprise e where e.adresse = :adrr")
    public void deleteEntreprise(@Param("adrr") String adrr);

    @Modifying
    @Query(value= "insert into entreprise(adresse) values (:adresse)", nativeQuery=true)
    public Entreprise ajouterEntreprise(@Param("adresse") String adresse);



}
