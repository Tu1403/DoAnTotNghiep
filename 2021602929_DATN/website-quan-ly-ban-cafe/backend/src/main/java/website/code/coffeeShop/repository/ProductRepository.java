package website.code.coffeeShop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import website.code.coffeeShop.model.Category;
import website.code.coffeeShop.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Iterable<Product> findByCategoryId(int cid);

    Page<Product> findByCategoryId(int cid, Pageable pageable);
// search
    @Query(value = "SELECT * FROM Product ORDER BY pid DESC LIMIT 3;", nativeQuery = true)
    List<Product> getTop3Products();

    Page<Product> findByPnameContainingIgnoreCase(String pname, Pageable pageable);

    Page<Product> findByPnameContaining(String pname, Pageable pageable);

    Page<Product> findByPnameContainingIgnoreCaseAndCategoryId(String pname, Integer cid, Pageable pageable);

    boolean existsByPname(String pname);

    boolean existsByPnameAndPidNot(String pname, int pid);
}
