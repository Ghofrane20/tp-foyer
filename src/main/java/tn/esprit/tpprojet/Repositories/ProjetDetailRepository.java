package tn.esprit.tpprojet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.tpprojet.Entities.ProjetDetail;

@Repository
public interface ProjetDetailRepository extends JpaRepository<ProjetDetail, Long> {
}
