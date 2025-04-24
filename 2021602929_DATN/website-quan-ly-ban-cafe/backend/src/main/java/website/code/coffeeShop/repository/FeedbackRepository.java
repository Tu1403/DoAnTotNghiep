package website.code.coffeeShop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.code.coffeeShop.dto.FeedbackDTO;
import website.code.coffeeShop.model.Feedback;

import java.util.Date;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    @Query("SELECT new website.code.coffeeShop.dto.FeedbackDTO(f, u.username) FROM Feedback f JOIN Users u ON f.userId = u.uid WHERE f.createdTime BETWEEN :startDate AND :endDate")
    Page<FeedbackDTO> findByCreatedTimeBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

    @Query("SELECT new website.code.coffeeShop.dto.FeedbackDTO(f, u.username) FROM Feedback f JOIN Users u ON f.userId = u.uid")
    Page<FeedbackDTO> findAllFeedbackWithUsername(Pageable pageable);

    @Query("SELECT new website.code.coffeeShop.dto.FeedbackDTO(f, u.username) FROM Feedback f JOIN Users u ON f.userId = u.uid WHERE f.userId = :userId")
    Page<FeedbackDTO> findFeedbackWithUsernameByUserId(@Param("userId") Integer userId, Pageable pageable);
}
