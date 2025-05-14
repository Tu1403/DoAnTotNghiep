package website.code.coffeeShop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import website.code.coffeeShop.model.Bill;

import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    Page<Bill> findByCreatedTimeBetween(Date startDate, Date endDate, Pageable pageable);

    Page<Bill> findByUserId(int userId, Pageable pageable);

    Page<Bill> findByPhoneContainingAndUserId(String phone, int userId, Pageable pageable);

    @Query("SELECT MONTH(b.createdTime) AS month, SUM(b.totalCost) AS totalRevenue "
            + "FROM Bill b GROUP BY MONTH(b.createdTime)")
    List<Object[]> findMonthlyRevenue(); //Tra ve mot mang doi tuong, gom month va totalCost

    @Query("SELECT MONTH(b.createdTime) AS month, COUNT(b.billId) AS totalOrders "
            + "FROM Bill b WHERE b.type = 0 GROUP BY MONTH(b.createdTime)") //COUNT(b.BillId) la tính tong so lan xuat hien cua billId LA PRIMARY KEY
    List<Object[]> findMonthlyOnlineOrders();

    @Query("SELECT MONTH(b.createdTime) AS month, COUNT(b.billId) AS totalOrders "
            + "FROM Bill b WHERE b.type = 1 GROUP BY MONTH(b.createdTime)")
    List<Object[]> findMonthlyOfflineOrders();
}
