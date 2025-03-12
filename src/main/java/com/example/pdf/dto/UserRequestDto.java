package com.example.pdf.dto;

import com.example.pdf.enums.Permission;
import com.example.pdf.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequestDto {
    private String username;
    private String email;
    private String password;
    private Role role;
    private Set<Permission> permissions;
} 