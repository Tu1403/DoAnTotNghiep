package website.code.coffeeShop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import website.code.coffeeShop.model.Users;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findByPhone(String phone);

    void deleteById(int uid);

    @Query("SELECT u FROM Users u WHERE u.role_id = ?1")
    Page<Users> findByRole(Integer roleId, Pageable pageable);

    @Query("SELECT u FROM Users u WHERE u.fullname LIKE %?1% OR u.email LIKE %?1% OR u.username LIKE %?1%")
    Page<Users> search(String keyword, Pageable pageable);

    @Query("SELECT u FROM Users u WHERE (u.fullname LIKE %?1% OR u.email LIKE %?1% OR u.username LIKE %?1%) AND u.role_id = ?2")
    Page<Users> searchByKeywordAndRole(String keyword, Integer roleId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO Users (fullname, dob, email, phone, address, avatar, username, pass, role_id, status)" +
            "VALUES (:fullname, :dob, :email, :phone, :address, :avatar, :username, :pass, :role_id, :status)", nativeQuery = true)
    void insertUsers(@Param("fullname") String fullname,
                     @Param("dob") Date dob,
                     @Param("email") String email,
                     @Param("phone") String phone,
                     @Param("address") String address,
                     @Param("avatar") String avatar,
                     @Param("username") String username,
                     @Param("pass") String pass,
                     @Param("role_id") int role_id,
                     @Param("status") int status);

}
