package website.code.coffeeShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import website.code.coffeeShop.model.Users;
import website.code.coffeeShop.service.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping
    public String registerUser(Users user, Model model) {

        user.setRole_id(1);
        user.setAvatar("abc");
        user.setStatus(1);

        boolean hasErrors = false;

        if (user.getUsername() == null || user.getPass() == null || user.getEmail() == null) {
            model.addAttribute("generalError", "Tài khoản và mật khẩu không được để trống !");
            hasErrors = true;
        }

        if (userService.isEmailTaken(user.getEmail())) {
            model.addAttribute("emailError", "Email đã được sử dụng");
            hasErrors = true;
        }

        if (userService.isPhoneTaken(user.getPhone())) {
            model.addAttribute("phoneError", "Số điện thoại đã được sử dụng");
            hasErrors = true;
        }

        if (userService.isUsernameTaken(user.getUsername())) {
            model.addAttribute("usernameError", "Username đã được sử dụng");
            hasErrors = true;
        }

        if (hasErrors) {
            model.addAttribute("user", user);
            return "register";
        }

        try {
            userService.saveUser(user);
        } catch (Exception e) {
            model.addAttribute("generalError", "Đăng kí tài khoản thất bại: " + e.getMessage());
            model.addAttribute("user", user);
            return "register";
        }

        return "redirect:/login";
    }
}
