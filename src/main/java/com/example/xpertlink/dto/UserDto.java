package com.example.xpertlink.dto;

import com.example.xpertlink.enums.Gender;
import com.example.xpertlink.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;

    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Full Name cannot be null")
    @Size(min = 1, max = 100, message = "Full Name must be between 1 and 100 characters")
    private String fullName;

    @Size(max = 255, message = "Address can be up to 255 characters")
    private String address;

    @Min(value = 0, message = "Age must be greater than or equal to 0")
    @Max(value = 150, message = "Age must be less than or equal to 150")
    private Integer age;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @NotNull(message = "Role cannot be null")
    private Role role;

    private boolean enabled;

    private String otpCode;

    @JsonFormat(pattern = "EEEE MMMM dd, yyyy HH:mm")
    private LocalDateTime otpExpiryTime;

    @JsonFormat(pattern = "EEEE MMMM dd, yyyy HH:mm")
    private LocalDateTime dateCreated;


    public UserDto(Long id, String username, String email, String fullName, String address, Integer age, Gender gender, Role role, boolean enabled, LocalDateTime otpExpiryTime, LocalDateTime dateCreated) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.address = address;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.enabled = enabled;
        this.otpExpiryTime = otpExpiryTime;
        this.dateCreated = dateCreated;
    }
}
