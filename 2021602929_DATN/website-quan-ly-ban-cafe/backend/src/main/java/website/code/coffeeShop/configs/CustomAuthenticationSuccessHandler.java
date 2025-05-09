package website.code.coffeeShop.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import website.code.coffeeShop.model.Users;
import website.code.coffeeShop.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Users user = userRepository.findByUsername(userDetails.getUsername());

        if(user.getRole_id() == 1) {
            response.sendRedirect("/homepage");
        }else if (user.getRole_id() == 2) {
            response.sendRedirect("/products");
        }else if(user.getRole_id() == 3) {
            response.sendRedirect("/management/table");
        }else if(user.getRole_id() == 4) {
            response.sendRedirect("/management");
        }else {
            response.sendRedirect("/management");
        }
    }
}
