package website.code.coffeeShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import website.code.coffeeShop.dto.ComplainDTO;
import website.code.coffeeShop.model.Complain;
import website.code.coffeeShop.model.Feedback;
import website.code.coffeeShop.repository.ComplainRepository;

import javax.validation.ValidationException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ComplainService {

    @Autowired
    private ComplainRepository complainRepository;

    public void save(Complain complain) {
        complainRepository.save(complain);
    }

    public void deleteById(int id) {
        complainRepository.deleteById(id);
    }

    public void saveComplainToDB(int userId, Date createdTime, String title, String complainUser, int status){
        validateCComplain(title, complainUser);
        Complain c = new Complain();
        c.setUserId(userId);
        c.setCreatedTime(createdTime);
        c.setTitle(title);
        c.setComplainUser(complainUser);
//        c.setRespon(respon);
        c.setStatus(0);

        complainRepository.save(c);
    }

    public void validateCComplain(String title, String complainUser){
        if(title == null || title.trim().isEmpty()){
            throw new ValidationException("Tiêu đề không được để trống !");
        }

        if(complainUser == null || complainUser.trim().isEmpty()){
            throw new ValidationException("Không được để trống !");
        }
    }

    public Page<Complain> getComplain(int userId, Pageable pageable){
        return complainRepository.findByUserId(userId, pageable);
    }

    public Page<ComplainDTO> getAllComplain2(Pageable pageable){
        return complainRepository.getAllComplains2(pageable);
    }

    public Page<ComplainDTO> getAllComplain(Pageable pageable){
        return complainRepository.getAllComplains(pageable);
    }

    public Page<ComplainDTO> getAllComplainByUserId(int userId, Pageable pageable){
        return complainRepository.getAllComplainByUserId(userId, pageable);
    }

    public Page<ComplainDTO> searchComplainByCreatedTime(Date createdTime, Pageable pageable){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();

        return complainRepository.findByCreatedTimeBetween(createdTime, endDate, pageable);
    }

    public Complain findComplainById(int id){
        return complainRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy mã complain:" + id));
    }
}
