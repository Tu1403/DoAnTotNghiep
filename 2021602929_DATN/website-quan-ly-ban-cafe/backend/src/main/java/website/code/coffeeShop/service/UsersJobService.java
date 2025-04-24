package website.code.coffeeShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import website.code.coffeeShop.model.UsersJob;
import website.code.coffeeShop.repository.UsersJobRepository;

@Service
public class UsersJobService {
    @Autowired
    private UsersJobRepository usersJobRepository;

    public UsersJob findByUsername(String userName) {return usersJobRepository.findByUsername(userName);}
}
