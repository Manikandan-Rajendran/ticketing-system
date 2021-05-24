package com.example.ticketingsystem.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired private UserRepository userRepository;

    @PostMapping("/api/v1/user")
    public User create(@RequestBody User user) {
        user.SetPassword();
        return userRepository.save(user);
    }

}
