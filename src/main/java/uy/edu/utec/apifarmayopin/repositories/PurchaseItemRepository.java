package uy.edu.utec.apifarmayopin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uy.edu.utec.apifarmayopin.models.PurchaseItem;

import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Integer> {
    List<PurchaseItem> findByProductIdOrderByPurchaseOrderDateDesc(Integer productId);
}