package uy.edu.utec.apifarmayopin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uy.edu.utec.apifarmayopin.models.Cart;
import uy.edu.utec.apifarmayopin.models.User;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser(User user);
}
