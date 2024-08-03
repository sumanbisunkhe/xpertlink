package com.example.xpertlink.service.impl;

import com.example.xpertlink.Repository.UserRepository;
import com.example.xpertlink.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomCustomerDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Autowired
    public CustomCustomerDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name()) // Convert enum Role to string
                .accountLocked(!user.isEnabled()) // Account locked if not enabled
                .build();
    }
}