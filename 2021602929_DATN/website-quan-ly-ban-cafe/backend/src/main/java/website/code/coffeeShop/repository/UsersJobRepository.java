package website.code.coffeeShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import website.code.coffeeShop.model.UsersJob;

@Repository
public interface UsersJobRepository extends JpaRepository<UsersJob, Integer> {
    UsersJob findByUsername(String username);
}
