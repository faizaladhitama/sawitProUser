package com.sawitPro.services;

import com.sawitPro.helpers.JwtUtil;
import com.sawitPro.models.User;
import com.sawitPro.repository.UserRepository;
import com.sawitPro.viewModels.Login;
import com.sawitPro.viewModels.Registration;
import com.sawitPro.viewModels.UpdateName;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtUtil jwtUtil){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public Boolean registration(Registration registration){
        String phoneNumber = registration.getPhoneNumber();
        String name = registration.getName();
        String password = registration.getPassword();
        boolean validation = phoneNumberValidation(phoneNumber) && nameValidation(name) && passwordValidation(password);
        if(!validation){
            return false;
        }
        User user = userRepository.findUserByPhoneNumber(phoneNumber);
        if(user != null){
            return false;
        }
        user = new User();
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return true;
    }

    public String login(Login login){
        String phoneNumber = login.getPhoneNumber();
        String password = login.getPassword();
        boolean validation = phoneNumberValidation(phoneNumber) && passwordValidation(password);
        if(!validation){
            return null;
        }
        User user = userRepository.findUserByPhoneNumber(phoneNumber);
        if(user == null){
            return null;
        }
        String hashedPassword = user.getPassword();
        boolean passwordValid = passwordEncoder.matches(password, hashedPassword);
        if(!passwordValid){
            return null;
        }
        return jwtUtil.generateToken(user);
    }

    public String getName(String authorizationHeader){
        boolean validate = jwtUtil.validateAuthorizationHeader(authorizationHeader);
        if(!validate){
            return null;
        }
        String token = jwtUtil.getTokenFromAuthorizationHeader(authorizationHeader);
        String jwtPhoneNumber = jwtUtil.getPhoneNumberFromToken(token);
        User user = userRepository.findUserByPhoneNumber(jwtPhoneNumber);
        return user == null ? null : user.getName();
    }

    public Boolean updateName(String authorizationHeader, UpdateName updateName){
        boolean validate = jwtUtil.validateAuthorizationHeader(authorizationHeader);
        if(!validate){
            return false;
        }
        String name = updateName.getName();
        if(!nameValidation(name)){
            return false;
        }
        String token = jwtUtil.getTokenFromAuthorizationHeader(authorizationHeader);
        String jwtPhoneNumber = jwtUtil.getPhoneNumberFromToken(token);
        User user = userRepository.findUserByPhoneNumber(jwtPhoneNumber);
        user.setName(updateName.getName());
        userRepository.save(user);
        return true;
    }

    private boolean phoneNumberValidation(String phoneNumber){
        if (StringUtils.isBlank(phoneNumber)){
            return false;
        }
        if (phoneNumber.length() < 10 || phoneNumber.length() > 13){
            return false;
        }
        return phoneNumber.startsWith("08");
    }

    private boolean nameValidation(String name){
        if (StringUtils.isBlank(name)){
            return false;
        }
        return name.length() <= 60;
    }

    private boolean passwordValidation(String password){
        if(StringUtils.isBlank(password)){
            return false;
        }
        if (password.length() < 6 || password.length() > 16){
            return false;
        }
        if(password.chars().noneMatch(Character::isUpperCase)){
            return false;
        }
        return password.chars().anyMatch(Character::isDigit);
    }
}
