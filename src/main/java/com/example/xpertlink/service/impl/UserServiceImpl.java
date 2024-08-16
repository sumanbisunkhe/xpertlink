package com.example.xpertlink.service.impl;

import com.example.xpertlink.Repository.UserRepository;
import com.example.xpertlink.dto.EmailDto;
import com.example.xpertlink.dto.UserDto;
import com.example.xpertlink.enums.Role;
import com.example.xpertlink.exceptions.UserNotFoundException;
import com.example.xpertlink.exceptions.UsernameNotFoundException;
import com.example.xpertlink.model.User;
import com.example.xpertlink.service.EmailService;
import com.example.xpertlink.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional

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
        // Find the user by ID
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Update user details
        boolean isChanged = false;

        if (!user.getFullName().equals(userDto.getFullName())) {
            user.setFullName(userDto.getFullName());
            isChanged = true;
        }

        if (!user.getAddress().equals(userDto.getAddress())) {
            user.setAddress(userDto.getAddress());
            isChanged = true;
        }

        if (!user.getAge().equals(userDto.getAge())) {
            user.setAge(userDto.getAge());
            isChanged = true;
        }

        if (!user.getGender().equals(userDto.getGender())) {
            user.setGender(userDto.getGender());
            isChanged = true;
        }

        if (!user.getRole().equals(userDto.getRole())) {
            user.setRole(userDto.getRole());
            isChanged = true;
        }

        if (!user.getEmail().equals(userDto.getEmail())) {
            user.setEmail(userDto.getEmail());
            isChanged = true;
        }

        if (!user.getUsername().equals(userDto.getUsername())) {
            user.setUsername(userDto.getUsername());
            isChanged = true;
        }

        // Update password if provided
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            isChanged = true;
        }

        if (isChanged) {
            // Disable the user and send OTP
            user.setEnabled(false);
            String otpCode =generateOtp();
            user.setOtpCode(otpCode);
            user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(5));
            sendOtpByEmail(user.getEmail(), otpCode);
            user.setDateCreated(user.getDateCreated());
        }

        // Save the updated user
        User updatedUser = userRepo.save(user);

        return ConvertToDto(updatedUser);
    }


    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepo.findById(id).map(this::ConvertToDto);
    }

    @Override
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepo.findByUsername(username).map(this::ConvertToDto);
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepo.findByEmail(email).map(this::ConvertToDto);
    }

    @Override
    public void deleteUserById(Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    @Override
    public void deleteUserByUsername(String username) {
        User user= userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with name: " + username));
        userRepo.delete(user);

    }

    @Override
    public UserDto EnableOrDisableUser(UserDto userDto) {
        User user = userRepo.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userDto.getId()));

        user.setEnabled(userDto.isEnabled());
        User updatedUser = userRepo.save(user);
        return ConvertToDto(updatedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream()
                .map(this::ConvertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllEmails() {
        return userRepo.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
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
