package com.example.xpertlink.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private Long id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String address;
    private String age;
    private Gender gender;
    private Role role;
    private boolean enabled;
    private String otpCode;
    private String otpExpiryTime;
    private String dateCreated;

}
