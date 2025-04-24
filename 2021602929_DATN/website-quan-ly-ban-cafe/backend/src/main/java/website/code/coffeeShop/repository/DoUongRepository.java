package website.code.coffeeShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.code.coffeeShop.model.DoUong;

import java.util.List;

@Repository
public interface DoUongRepository extends JpaRepository<DoUong, Long> {
    @Override
    long count();

    @Query(value = "select  n from DoUong n ")
    List<DoUong> getListDoUong();

    @Query(value = "select  ten from DoUong where id =:id", nativeQuery = true)
    String getNameById(@Param("id") Long id);
}
