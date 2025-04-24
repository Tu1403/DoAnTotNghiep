package website.code.coffeeShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import website.code.coffeeShop.model.Cart;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Iterable<Cart> findByUid(int uid);
    Optional<Cart> findByPidAndUid(int pid, int uid);
}
