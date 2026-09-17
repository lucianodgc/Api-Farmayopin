package uy.edu.utec.apifarmayopin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uy.edu.utec.apifarmayopin.models.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    boolean existsByName(String name);
    List<Product> findByNameContainingIgnoreCase(String name);
}
