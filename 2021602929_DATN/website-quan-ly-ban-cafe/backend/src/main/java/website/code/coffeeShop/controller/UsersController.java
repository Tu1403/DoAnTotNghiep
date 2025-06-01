package website.code.coffeeShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import website.code.coffeeShop.model.Bill;
import website.code.coffeeShop.model.BillDetail;
import website.code.coffeeShop.model.Complain;
import website.code.coffeeShop.model.Users;
import website.code.coffeeShop.service.*;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private BillService billService;

    @Autowired
    private BillDetailService billDetailService;

    @Autowired
    private ComplainService complainService;
    @GetMapping
    public String viewProfile(Model model, Principal principal) {
        String username = principal.getName();
        Users user = userService.findByUsername(username);
        int userId = user.getUid();
        List<CartItemService.CartItemWithProduct> cartItems = cartItemService.getCartItemsByCustomerId(userId);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user", user);
        model.addAttribute("currentPage", "profile");
        return "profile";
    }

    @PostMapping("/edit")
    public String editProfile(@RequestParam("fullname") String fullname,
                              @RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob,
                              @RequestParam("address") String address,
                              @RequestParam("email") String email,
                              @RequestParam("phone") String phone,
                              @RequestParam("avatar") MultipartFile avatarFile,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Users existingUser = userService.findByUsername(username);
        boolean hasErrors = false;
        // Validation
        if (!email.equals(existingUser.getEmail()) && userService.isEmailTaken(email)) {
            redirectAttributes.addFlashAttribute("emailError", "Email đã được sử dụng");
            hasErrors = true;
        }
        if (!phone.equals(existingUser.getPhone()) && userService.isPhoneTaken(phone)) {
            redirectAttributes.addFlashAttribute("phoneError", "Số điện thoại đã được sử dụng");
            hasErrors = true;
        }
        // Update if no errors
        if (!hasErrors) {
            existingUser.setFullname(fullname);
            existingUser.setDob(dob);
            existingUser.setAddress(address);
            existingUser.setEmail(email);
            existingUser.setPhone(phone);
            if (!avatarFile.isEmpty()) {
                try {
                    String avatarBase64 = Base64.getEncoder().encodeToString(avatarFile.getBytes());
                    existingUser.setAvatar(avatarBase64);
                } catch (IOException e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("message", "Đăng tải avatar thất bại");
                    return "redirect:/profile";
                }
            }
            userService.save1(existingUser);
            redirectAttributes.addFlashAttribute("message", "Cập nhật profile thành công!");
            return "redirect:/profile";
        }
        return "redirect:/profile?error=true";

    }

    @GetMapping("/changePassword")
    public String showChangePasswordForm() {
        return "changePassword"; // Ensure this view exists
    }


    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        Users user = userService.findByUsername(username); // Get the current user

        // Check if the current password is correct
        if (!userService.checkPassword(user, currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "Nhập sai mật khẩu !");
            return "redirect:/profile/changePassword";
        }

        // Check if the new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận mật khẩu xác nhận không giống nhau !");
            return "redirect:/profile/changePassword";
        }

        // Check if the new password is not the same as the current password
        if (newPassword.equals(currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không thể giống mật khẩu cũ được !");
            return "redirect:/profile/changePassword";
        }

        // Update the password
        userService.updatePassword(user, newPassword);
        redirectAttributes.addFlashAttribute("message", "Thay đổi mật khẩu thành công!");

        return "redirect:/profile";
    }

    @GetMapping("/history")
    public String getUserBills(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(required = false) String phone,
                               Model model, Principal principal) {
        String username = principal.getName();
        Users user = userService.findByUsername(username);
        int userId = user.getUid();
        List<CartItemService.CartItemWithProduct> cartItems = cartItemService.getCartItemsByCustomerId(userId);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user", user);
        model.addAttribute("page", "profile");

//        PageRequest pageable = PageRequest.of(page, 5); // 5 bills per page
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdTime"));

        Page<Bill> bills = null;
        String errorMessage = null;

        if (phone != null && !phone.isEmpty()) {
            bills = billService.searchBillsByPhoneAndUserId(phone, userId, pageable);
            if (bills.isEmpty()) {
                errorMessage = "Số điện thoại không tồn tại!";
            }
            model.addAttribute("searchQuery", phone);
        } else {
            bills = billService.getBillsByUserId(userId, pageable);
        }

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        model.addAttribute("bills", bills);
        model.addAttribute("currentPage", page);
        return "orderhistory";
    }

    @GetMapping("/bill/details/{billId}")
    @ResponseBody
    public List<BillDetail> getBillDetails(@PathVariable int billId) {
        return billDetailService.findByBillId(billId);
    }

    @GetMapping("/complainUser")
    public String ShowComplain(@RequestParam(defaultValue = "1") int pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
                               @RequestParam(required = false) String keyword,
                               Model model, Principal principal){
        String username = principal.getName();
        Users user = userService.findByUsername(username);
        int userId = user.getUid();
        List<CartItemService.CartItemWithProduct> cartItems = cartItemService.getCartItemsByCustomerId(userId);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user", user);
        model.addAttribute("page", "profile");

        //5 complain per page
        PageRequest pageable = PageRequest.of(pageNo - 1, pageSize);


        Page<Complain> complains = complainService.getComplain(userId, pageable);

        model.addAttribute("complains", complains.getContent());
        model.addAttribute("totalPage", complains.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("pageSize", pageSize);
        return "complainHistory";
    }
}
