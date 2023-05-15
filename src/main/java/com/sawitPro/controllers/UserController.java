package com.sawitPro.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sawitPro.models.User;
import com.sawitPro.repository.UserRepository;
import com.sawitPro.services.UserService;
import com.sawitPro.viewModels.Login;
import com.sawitPro.viewModels.Registration;
import com.sawitPro.viewModels.UpdateName;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @GetMapping("/name")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public String getName(@Parameter(hidden = true) @RequestHeader(value = "Authorization") String bearerToken){
        return userService.getName(bearerToken);
    }

    @PutMapping("/name")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public Boolean updateName(@Parameter(hidden = true) @RequestHeader(value = "Authorization") String bearerToken,
                              @RequestBody UpdateName updateName){
        return userService.updateName(bearerToken, updateName);
    }
}
