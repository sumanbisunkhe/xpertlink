package com.example.xpertlink.controller;

import com.example.xpertlink.dto.UserDto;
import com.example.xpertlink.jwt.AuthenticationRequest;
import com.example.xpertlink.jwt.AuthenticationResponse;
import com.example.xpertlink.jwt.JwtUtil;
import com.example.xpertlink.model.User;
import com.example.xpertlink.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;



    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        ResponseEntity<Map<String, Object>> response1 = getMapResponseEntity(bindingResult);
        if (response1 != null) return response1;

        UserDto createdUser = userService.registerUser(userDto);

        if (userDto.getOtpCode() != null) {
            boolean isVerified = userService.verifyOtp(userDto.getEmail(), userDto.getOtpCode());
            if (isVerified) {
                Map<String, Object> response = Map.of(
                        "message", "Account " + userDto.getUsername() + " created and email verified successfully.",
                        "data", createdUser
                );
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                Map<String, Object> response = Map.of(
                        "message", "Invalid or expired OTP."
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        Map<String, Object> response = Map.of(
                "message", "Account " + userDto.getUsername() + " created successfully. Please verify your email with the OTP sent.",
                "data", createdUser
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private ResponseEntity<Map<String, Object>> getMapResponseEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Validation error");
            response.put("errors", errors);
            return ResponseEntity.badRequest().body(response);
        }
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );

            // Generate JWT token
            String token = jwtUtil.generateToken(authenticationRequest.getUsername());

            // Return the token in the response
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (BadCredentialsException e) {
            // Handle authentication failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto,
            BindingResult bindingResult) {

        // Handle validation errors
        ResponseEntity<Map<String, Object>> response = getMapResponseEntity(bindingResult);
        if (response != null) return response;

        // Update user
        UserDto updatedUser = userService.updateUser(id, userDto);

        // Return response
        Map<String, Object> output = Map.of(
                "message", "User information updated successfully. Verify your account with the otp sent to your Email.",
                "data", updatedUser
        );
        return new ResponseEntity<>(output, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        Map<String, Object> response = Map.of(
                "message", "Users list fetched successfully.",
                "data", users
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        Map<String, Object> response = Map.of(
                "message", "User with ID "+id+" fetched successfully.",
                "data", user
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Map<String, Object>> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        Map<String, Object> response = Map.of(
                "message", "User with ID " + id + " has been successfully deleted."
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/delete/username/{username}")
    public ResponseEntity<Map<String, Object>> deleteUserByUsername(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        Map<String, Object> response = Map.of(
                "message", "User with username " + username + " has been successfully deleted."
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/emails")
    public ResponseEntity<Map<String, Object>> getAllEmails() {
        List<String> emails = userService.getAllEmails();
        Map<String, Object> response = Map.of(
                "message", "User emails fetched successfully.",
                "data", emails
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<Map<String, String>> generateOtp(@RequestParam String email) {
        userService.generateOtpAndSendEmail(email);
        Map<String, String> response = Map.of("message", "OTP generated and sent to email.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> otpRequest) {
        String email = otpRequest.get("email");
        String otp = otpRequest.get("otp_code");

        boolean isVerified = userService.verifyOtp(email, otp);
        if (isVerified) {
            return ResponseEntity.ok("Email verified successfully. User registration is complete.");
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.WWW_AUTHENTICATE, "OTP realm=\"example\"");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .headers(headers)
                    .body("Invalid or expired OTP.");
        }
    }


}
