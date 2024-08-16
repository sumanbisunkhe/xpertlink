package com.example.xpertlink.service.impl;

import com.example.xpertlink.Repository.UserRepository;
import com.example.xpertlink.model.User;
import com.example.xpertlink.enums.Gender;
import com.example.xpertlink.enums.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        if (!userRepository.existsByEmail("sumanbisunkhe304@gmail.com")&&
                !userRepository.existsByUsername("Suman")) {
            User user = new User();
            user.setUsername("Suman");
            user.setPassword("$2a$12$ywy8BVxR0xL/ja6bfWlBjOKlos8LkSoQ2warr9c8XHQha6U3kIRy."); // Example hashed password
            user.setEmail("sumanbisunkhe304@gmail.com");
            user.setFullName("Suman Bisunkhe");
            user.setAddress("Nepal");
            user.setAge(21);
            user.setGender(Gender.MALE);
            user.setRole(Role.ADMIN);
            user.setEnabled(true);
            user.setOtpCode(null);
            user.setOtpExpiryTime(null);
            user.setDateCreated(LocalDateTime.now());
            userRepository.save(user);
        }

        if (!userRepository.existsByEmail("sbisunkhe54@gmail.com")&&
                !userRepository.existsByUsername("SumanB")) {
            User user = new User();
            user.setUsername("SumanB");
            user.setPassword("$2a$12$ywy8BVxR0xL/ja6bfWlBjOKlos8LkSoQ2warr9c8XHQha6U3kIRy."); // Example hashed password
            user.setEmail("sbisunkhe54@gmail.com");
            user.setFullName("Suman Bisunkhe");
            user.setAddress("Nepal");
            user.setAge(21);
            user.setGender(Gender.MALE);
            user.setRole(Role.USER);
            user.setEnabled(true);
            user.setOtpCode(null);
            user.setOtpExpiryTime(null);
            user.setDateCreated(LocalDateTime.now());
            userRepository.save(user);
        }
    }
}
