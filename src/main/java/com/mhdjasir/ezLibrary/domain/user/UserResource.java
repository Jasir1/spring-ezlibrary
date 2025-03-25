package com.mhdjasir.ezLibrary.domain.user;

import com.mhdjasir.ezLibrary.domain.security.service.AuthService;
import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "ezLibrary")
public class UserResource {
    private final UserService userService;
    private final AuthService authService;

    @PutMapping("/update")
    public ResponseEntity<ApplicationResponseDTO> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(userService.updateUser(userUpdateDTO));
    }
    @PutMapping("/update-profile-pic")
    public ResponseEntity<ApplicationResponseDTO> updateProfilePic(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(userService.updateProfilePic(file));
    }
    @PutMapping("/reset-password")
    public ResponseEntity<ApplicationResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        return ResponseEntity.ok(authService.resetPassword(resetPasswordDTO));
    }
    @GetMapping("/get")
    public ResponseEntity<UserResponseDTO> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }
}
