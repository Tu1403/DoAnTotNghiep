package website.code.coffeeShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import website.code.coffeeShop.model.Tables;

@Repository
public interface TableRepository extends JpaRepository<Tables, Integer> {
}
