package com.example.pdf.dto;

import com.example.pdf.enums.Role;
import com.example.pdf.enums.Permission;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Set<Permission> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
