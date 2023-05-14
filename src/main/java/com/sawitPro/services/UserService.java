package com.sawitPro.services;

import com.sawitPro.models.User;
import com.sawitPro.repository.UserRepository;
import com.sawitPro.viewModels.Registration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.stream.IntStream;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Boolean registration(Registration registration){
        String phoneNumber = registration.getPhoneNumber();
        String name = registration.getName();
        String password = registration.getPassword();
        boolean validation = phoneNumberValidation(phoneNumber) || nameValidation(name) || passwordValidation(password);
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
        IntStream passwordChars = password.chars();
        if(passwordChars.noneMatch(Character::isUpperCase)){
            return false;
        }
        return passwordChars.anyMatch(Character::isDigit);
    }
}
