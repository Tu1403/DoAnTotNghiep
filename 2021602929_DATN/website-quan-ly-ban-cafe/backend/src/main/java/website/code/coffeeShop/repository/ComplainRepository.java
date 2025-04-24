package website.code.coffeeShop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.code.coffeeShop.dto.ComplainDTO;
import website.code.coffeeShop.model.Complain;

import java.util.Date;

@Repository
public interface ComplainRepository extends JpaRepository<Complain, Integer> {
    Page<Complain> findByUserId(int userId, Pageable pageable);

    @Query("SELECT c, u.username FROM Complain c JOIN Users u ON c.userId = u.uid")
    Page<ComplainDTO> getAllComplains2(Pageable pageable);

    @Query("SELECT new website.code.coffeeShop.dto.ComplainDTO(c, u.username) FROM Complain c JOIN Users u ON c.userId = u.uid " + "WHERE c.createdTime BETWEEN :startDate AND :endDate")
    Page<ComplainDTO> findByCreatedTimeBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

    @Query("SELECT new website.code.coffeeShop.dto.ComplainDTO(c, u.username) FROM Complain c JOIN Users u ON c.userId = u.uid")
    Page<ComplainDTO> getAllComplains(Pageable pageable);

    @Query("SELECT new website.code.coffeeShop.dto.ComplainDTO(c, u.username) FROM Complain c JOIN Users u ON c.userId = u.uid WHERE c.userId = :userId")
    Page<ComplainDTO> getAllComplainByUserId(@Param("userId") int userId, Pageable pageable);
}
