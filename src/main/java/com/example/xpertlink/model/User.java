package com.example.xpertlink.model;

import com.example.xpertlink.enums.Gender;
import com.example.xpertlink.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "users",
        uniqueConstraints={
                @UniqueConstraint(name = "UNIQUE_username",columnNames ="username" ),
                @UniqueConstraint(name = "UNIQUE_email",columnNames ="email" )

        }

)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String fullName;

    private String address;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled;

    private String otpCode;

    private LocalDateTime otpExpiryTime;

    private LocalDateTime dateCreated;
}
