package com.example.xpertlink.service.impl;

import com.example.xpertlink.Repository.UserRepository;
import com.example.xpertlink.dto.EmailDto;
import com.example.xpertlink.dto.UserDto;
import com.example.xpertlink.exceptions.UserNotFoundException;
import com.example.xpertlink.model.User;
import com.example.xpertlink.service.EmailService;
import com.example.xpertlink.service.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepo, JavaMailSender javaMailSender, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepo = userRepo;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    @Override
    public UserDto registerUser(UserDto userDto) {
        // Check if user exists by email or username
        Optional<User> existingUserByEmail = userRepo.findByEmail(userDto.getEmail());
        Optional<User> existingUserByUsername = userRepo.findByUsername(userDto.getUsername());

        User user;

        if (existingUserByEmail.isPresent() || existingUserByUsername.isPresent()) {
            if (existingUserByEmail.isPresent()) {
                user = existingUserByEmail.get();
            } else {
                user = existingUserByUsername.get();
            }

            // Update user details
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setFullName(userDto.getFullName());
            user.setAddress(userDto.getAddress());
            user.setAge(userDto.getAge());
            user.setGender(userDto.getGender());
            user.setRole(userDto.getRole());
            user.setEnabled(false); // Make sure enabled is false
            user.setOtpCode(generateOtp());
            user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5)); // Set OTP expiry time
            user.setDateCreated(LocalDateTime.now()); // Update creation time if necessary

            // Save user and send OTP email
            User updatedUser = userRepo.save(user);
            sendOtpByEmail(updatedUser.getEmail(), updatedUser.getOtpCode());

            return ConvertToDto(updatedUser);
        } else {
            // Create new user
            user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setEmail(userDto.getEmail());
            user.setFullName(userDto.getFullName());
            user.setAddress(userDto.getAddress());
            user.setAge(userDto.getAge());
            user.setGender(userDto.getGender());
            user.setRole(userDto.getRole());
            user.setEnabled(false);
            user.setOtpCode(generateOtp());
            user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
            user.setDateCreated(LocalDateTime.now());

            // Save user and send OTP email
            User savedUser = userRepo.save(user);
            sendOtpByEmail(savedUser.getEmail(), savedUser.getOtpCode());

            return ConvertToDto(savedUser);
        }
    }


    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        return null;
    }

    @Override
    public UserDto getUserById(Long id) {
        return null;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void deleteUserById(Long id) {

    }

    @Override
    public void deleteUserByUsername(String username) {

    }

    @Override
    public UserDto EnableOrDisableUser(UserDto userDto) {
        return null;
    }


    // OTP generation and email sending
    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(999999); // Generates a random number between 0 and 999999
        return String.format("%06d", otp); // Pads the number with leading zeros if necessary
    }

    private void sendOtpByEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        javaMailSender.send(message);
    }

    @Override
    public void generateOtpAndSendEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        user.setOtpCode(generateOtp());
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
        userRepo.save(user);
        sendOtpByEmail(user.getEmail(), user.getOtpCode());
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Check if OTP is expired or not set
        if (user.getOtpCode() == null || user.getOtpExpiryTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Check if OTP matches
        boolean isValid = user.getOtpCode().equals(otp);
        if (isValid) {
            // Clear OTP fields after successful verification
            user.setOtpCode(null);
            user.setOtpExpiryTime(null);
            user.setEnabled(true);
            userRepo.save(user);

            //Send welcome email
            String message = String.format("Hello %s,\n\nWelcome to XpertLink. Let's Connect, Learn and Grow.", user.getUsername());
            EmailDto emailDto = new EmailDto(user.getEmail(), "Welcome to XpertLink", message);
            emailService.sendEmail(emailDto);
        }
        return isValid;
    }



    @Override
    public UserDetails loadUserByUsername(String username) {
        return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return List.of();
    }

    @Override
    public List<String> getAllEmails() {
        return List.of();
    }


    private UserDto ConvertToDto (User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getAddress(),
                user.getAge(),
                user.getGender(),
                user.getRole(),
                user.isEnabled(),
                user.getOtpExpiryTime(),
                user.getDateCreated()

        );



    }
}
