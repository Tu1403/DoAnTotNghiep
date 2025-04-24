package website.code.coffeeShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import website.code.coffeeShop.dto.FeedbackDTO;
import website.code.coffeeShop.model.Feedback;
import website.code.coffeeShop.model.Product;
import website.code.coffeeShop.repository.FeedbackRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public void save(Feedback feedback) {
        feedbackRepository.save(feedback);
    }

    public void deleteById(int id) {
        feedbackRepository.deleteById(id);
    }

    public Page<FeedbackDTO> getAllFeedbackWithUsername(Pageable pageable) {
        return feedbackRepository.findAllFeedbackWithUsername(pageable);
    }

    public Page<FeedbackDTO> getFeedbackByUserId(Integer userId, Pageable pageable) {
        return feedbackRepository.findFeedbackWithUsernameByUserId(userId, pageable);
    }

    public Page<FeedbackDTO> searchByCreatedTime(Date date, Pageable pageable) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();

        return feedbackRepository.findByCreatedTimeBetween(startDate, endDate, pageable);
    }

    public Feedback findFeedbackById(int id) {
        return feedbackRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy mã feedback:" + id));
    }

}
