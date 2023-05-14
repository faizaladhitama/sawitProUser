package com.sawitPro.controllers;

import com.sawitPro.models.User;
import com.sawitPro.repository.UserRepository;
import com.sawitPro.services.UserService;
import com.sawitPro.viewModels.Login;
import com.sawitPro.viewModels.Registration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userControllerV1")
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/registration")
    public Boolean registration(@RequestBody Registration registration){
       return userService.registration(registration);
    }

    @PostMapping("/login")
    public String login(@RequestBody Login login){
        return userService.login(login);
    }
}
