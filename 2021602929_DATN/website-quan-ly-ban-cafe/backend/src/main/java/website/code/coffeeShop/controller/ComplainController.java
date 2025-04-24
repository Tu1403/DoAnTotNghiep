package website.code.coffeeShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import website.code.coffeeShop.dto.ComplainDTO;
import website.code.coffeeShop.dto.FeedbackDTO;
import website.code.coffeeShop.model.Bill;
import website.code.coffeeShop.model.Complain;
import website.code.coffeeShop.model.Users;
import website.code.coffeeShop.service.*;

import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
public class ComplainController {
    @Autowired
    private ComplainService complainService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/complain")
    public String getComplain(@RequestParam(defaultValue = "0") int page, Model model, Principal principal) {
        String username = principal.getName();
        Users user = userService.findByUsername(username);
        int userId = user.getUid();
        List<CartItemService.CartItemWithProduct> cartItems = cartItemService.getCartItemsByCustomerId(userId);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user", user);
        model.addAttribute("page", "complain");
        model.addAttribute("currentPage", page);
        model.addAttribute("complain", new Complain());
        return "complain";
    }

    @PostMapping("/complain/add")
    public String addomplain(@ModelAttribute("complain") Complain complain,
                             BindingResult result,
                             Model model,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Users user = userService.findByUsername(username);
        int userId = user.getUid();

        if(result.hasErrors()) {
            model.addAttribute("errormessage", "Bạn phải nhập đầy đủ thông tin");
            model.addAttribute("complain", complain);
            return "complain";
        }

        try{
            complainService.saveComplainToDB(userId, new Date(), complain.getTitle(), complain.getComplainUser(), 0);
            redirectAttributes.addFlashAttribute("message", "Phản hồi thành công !");
            return "redirect:/complain";
        }catch(ValidationException e){
            model.addAttribute("errormessage", e.getMessage());
            model.addAttribute("complain", complain);
            return "complain";
        }
    }

    @GetMapping("/respon")
    public String getRespon(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            @RequestParam(value = "createdTime", required = false) String createdTime,
                            Model model, Principal principal){
        if(principal != null){
            String username = principal.getName();
            Users user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<ComplainDTO> respon;
        boolean noRespon = false;
        if(createdTime != null && !createdTime.isEmpty()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(createdTime, formatter);
            Date dateValue = java.sql.Date.valueOf(date);
            respon = complainService.searchComplainByCreatedTime(dateValue, pageable);
            noRespon = respon.isEmpty();
        }else {
            respon = complainService.getAllComplain(pageable);
        }
        model.addAttribute("respons", respon.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", respon.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("createdTime", createdTime);
        model.addAttribute("nofeedback", noRespon);
        return "respon";
    }

    @GetMapping("/respon/searchDate")
    public String findFeedbackByUserId(@RequestParam(value = "userId", required = false) Integer userId,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "size", defaultValue = "10") int size,
                                       @RequestParam(value = "createdTime", required = false) String createdTime,
                                       Model model,
                                       Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Users user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        Pageable pageable = PageRequest.of(page, size);
        if (userId == null){
            return "redirect:/respon";
        }
        Page<ComplainDTO> respons = complainService.getAllComplainByUserId(userId, pageable);
        model.addAttribute("respons", respons);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", respons.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("createdTime", createdTime);
        return "respon";
    }

    @PostMapping("/respon/sendRespon")
    public String savedComplain(@RequestParam(value = "cid", required = false) int cid,
                                @RequestParam("complain") String complain,
                                @RequestParam("respon") String respon,
                                RedirectAttributes redirectAttributes){
        Complain complain1 = complainService.findComplainById(cid);
        try{
            if (complain1 == null) {
                throw new RuntimeException("Không thể tìm thấy: " + cid);
            }
            if(respon != null && !respon.isEmpty()){
                complain1.setComplainUser(complain);
                complain1.setRespon(respon);
                complain1.setStatus(1);
                complainService.save(complain1);
                redirectAttributes.addFlashAttribute("message", "Phản hồi khách hàng thành công.");
            }else {
                redirectAttributes.addFlashAttribute("errormessage", "Bạn không nhập thì gửi bằng mắt");
            }

        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Có lỗi gì đó rồi.");
        }
        return "redirect:/respon";
    }

    @GetMapping("/respon/delete/{id}")
    public String deleteComplain(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try{
            complainService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xóa thành công.");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","Xóa thất bại.");
        }
        return "redirect:/respon";
    }
}
