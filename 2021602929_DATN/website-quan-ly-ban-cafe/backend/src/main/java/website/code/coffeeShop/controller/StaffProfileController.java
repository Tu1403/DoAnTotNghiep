package website.code.coffeeShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import website.code.coffeeShop.model.Users;
import website.code.coffeeShop.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Date;

@Controller
@RequestMapping("/staffProfile")
public class StaffProfileController {
    @Autowired
    private UserService userService;
    @GetMapping
    public String viewStaffProfile(Model model, Principal principal){
        String username = principal.getName();
        Users user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "staffProfile";
    }

    @PostMapping("/edit")
    public String editStaffProfile(@RequestParam("fullname") String fullname,
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
                    return "redirect:/staffProfile";
                }
            }
            userService.save1(existingUser);
            redirectAttributes.addFlashAttribute("message", "Cập nhật profile thành công!");
            return "redirect:/staffProfile";
        }
        return "redirect:/staffProfile?error=true";

    }

    @GetMapping("/staffChangePassword")
    public String showChangePasswordForm() {
        return "staffChangePassword"; // Ensure this view exists
    }


    @PostMapping("/staffChangePassword")
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
            return "redirect:/staffProfile/staffChangePassword";
        }

        // Check if the new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không giống nhau !");
            return "redirect:/staffProfile/staffChangePassword";
        }

        // Check if the new password is not the same as the current password
        if (newPassword.equals(currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không thể giống mật khẩu cũ !");
            return "redirect:/staffProfile/staffChangePassword";
        }

        // Update the password
        userService.updatePassword(user, newPassword);
        redirectAttributes.addFlashAttribute("message", "Đổi mật khẩu thành công !");

        return "redirect:/staffProfile";
    }

}
