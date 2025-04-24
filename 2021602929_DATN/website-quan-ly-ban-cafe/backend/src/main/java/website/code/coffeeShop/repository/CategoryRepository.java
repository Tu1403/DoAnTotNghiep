package website.code.coffeeShop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import website.code.coffeeShop.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findCategoriesByCategoryName(String name);

    Page<Category> findByCategoryNameContainingIgnoreCase(String keyword, Pageable pageable);
}
