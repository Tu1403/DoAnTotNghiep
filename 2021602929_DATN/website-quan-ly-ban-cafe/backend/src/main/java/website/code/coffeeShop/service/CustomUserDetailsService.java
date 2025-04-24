package website.code.coffeeShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import website.code.coffeeShop.model.Users;
import website.code.coffeeShop.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Không thể tìm thấy Người dùng !");
        }
        if (user.getStatus() != 1) {
            throw new UsernameNotFoundException("Tài khoản người dùng không hoạt động !");
        }
        return User.builder()
                .username(user.getUsername())
                .password(user.getPass())
                .roles(user.getRole_id() == 0 ? "USER" : "ADMIN")
                .build();
    }
}
