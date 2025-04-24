package website.code.coffeeShop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import website.code.coffeeShop.dto.FeedbackDTO;
import website.code.coffeeShop.model.*;
import website.code.coffeeShop.repository.FeedbackRepository;
import website.code.coffeeShop.service.BillDetailService;
import website.code.coffeeShop.service.BillService;
import website.code.coffeeShop.service.FeedbackService;
import website.code.coffeeShop.service.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FeedbackController {
    private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

//    @GetMapping("/respon")
//    public String allFeedback(@RequestParam(value = "page", defaultValue = "0") int page,
//                              @RequestParam(value = "size", defaultValue = "10") int size,
//                              @RequestParam(value = "createdTime", required = false) String createdTime,
//                              Model model, Principal principal) {
//        if (principal != null) {
//            String username = principal.getName();
//            Users user = userService.findByUsername(username);
//            model.addAttribute("user", user);
//        }
//        Pageable pageable = PageRequest.of(page, size);
//        Page<FeedbackDTO> feedbackPage;
//        boolean nofeedback = false;
//        if (createdTime != null && !createdTime.isEmpty()) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            LocalDate date = LocalDate.parse(createdTime, formatter);
//            Date dateValue = java.sql.Date.valueOf(date);
//            feedbackPage = feedbackService.searchByCreatedTime(dateValue, pageable);
//            nofeedback = feedbackPage.isEmpty();
//        }  else {
//            feedbackPage = feedbackService.getAllFeedbackWithUsername(pageable);
//        }
//        model.addAttribute("feedbacks", feedbackPage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", feedbackPage.getTotalPages());
//        model.addAttribute("size", size);
//        model.addAttribute("createdTime", createdTime);
//        model.addAttribute("nofeedback", nofeedback);
//        return "respon";
//    }
//
//    @GetMapping("/respon/searchDate")
//    public String findFeedbackByUserId(@RequestParam(value = "userId", required = false) Integer userId,
//                                       @RequestParam(value = "page", defaultValue = "0") int page,
//                                       @RequestParam(value = "size", defaultValue = "10") int size,
//                                       @RequestParam(value = "createdTime", required = false) String createdTime,
//                                       Model model,
//                                       Principal principal) {
//        if (principal != null) {
//            String username = principal.getName();
//            Users user = userService.findByUsername(username);
//            model.addAttribute("user", user);
//        }
//        Pageable pageable = PageRequest.of(page, size);
//        if (userId == null){
//            return "redirect:/respon";
//        }
//        Page<FeedbackDTO> feedbacks = feedbackService.getFeedbackByUserId(userId, pageable);
//        model.addAttribute("feedbacks", feedbacks);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", feedbacks.getTotalPages());
//        model.addAttribute("size", size);
//        model.addAttribute("createdTime", createdTime);
//        return "respon";
//    }
//
//    @PostMapping("/respon/complain")
//    public String savedComplain(@RequestParam(value = "fid", required = false) int fid,
//                                @RequestParam("complain") String complain,
//                                @RequestParam("respon") String respon,
//                                RedirectAttributes redirectAttributes){
//        Feedback feedback = feedbackService.findFeedbackById(fid);
//        try{
//            if (feedback == null) {
//                throw new RuntimeException("Không thể tìm thấy: " + fid);
//            }
//            feedback.setComplain(complain);
//            feedback.setRespon(respon);
//            feedback.setStatus(1);
//            feedbackService.save(feedback);
//            redirectAttributes.addFlashAttribute("message", "Phản hồi khách hàng thành công.");
//        }catch (Exception e){
//            redirectAttributes.addFlashAttribute("error", "Có lỗi gì đó rồi.");
//        }
//        return "redirect:/respon";
//    }
//
//    @GetMapping("/respon/delete/{id}")
//    public String deleteComplain(@PathVariable int id, RedirectAttributes redirectAttributes) {
//        try{
//            feedbackService.deleteById(id);
//            redirectAttributes.addFlashAttribute("message", "Xóa thành công.");
//        }catch (Exception e){
//            redirectAttributes.addFlashAttribute("error","Xóa thất bại.");
//        }
//        return "redirect:/respon";
//    }
}
