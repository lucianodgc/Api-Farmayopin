package uy.edu.utec.apifarmayopin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uy.edu.utec.apifarmayopin.models.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {
}
