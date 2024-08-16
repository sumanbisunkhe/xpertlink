package com.example.xpertlink.service;

import com.example.xpertlink.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto registerUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    Optional<UserDto> getUserById(Long id);
    Optional<UserDto> getUserByUsername(String username);
    Optional<UserDto> getUserByEmail(String email);
    void deleteUserById(Long id);
    void deleteUserByUsername(String username);
    UserDto  EnableOrDisableUser(UserDto userDto);

    void generateOtpAndSendEmail(String email);

    boolean verifyOtp(String email, String otp);

    UserDetails loadUserByUsername(String username);

    List<UserDto> getAllUsers();

    List<String> getAllEmails();
}
