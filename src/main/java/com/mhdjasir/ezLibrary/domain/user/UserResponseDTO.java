package com.mhdjasir.ezLibrary.domain.user;

import com.mhdjasir.ezLibrary.domain.security.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String primaryAddress;
    private String secondaryAddress;
    private String imagePath;
    private UserRole userRole;
    private Boolean status;
    private Boolean delete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
