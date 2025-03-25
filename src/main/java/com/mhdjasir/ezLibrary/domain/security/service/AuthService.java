package com.mhdjasir.ezLibrary.domain.security.service;

import com.mhdjasir.ezLibrary.domain.security.dto.AuthResponseDTO;
import com.mhdjasir.ezLibrary.domain.security.dto.LogInDTO;
import com.mhdjasir.ezLibrary.domain.security.dto.ResetForgotPasswordDTO;
import com.mhdjasir.ezLibrary.domain.security.entity.User;
import com.mhdjasir.ezLibrary.domain.security.entity.UserRole;
import com.mhdjasir.ezLibrary.domain.security.repos.UserRepository;
import com.mhdjasir.ezLibrary.domain.security.rest.AuthRequestDTO;
import com.mhdjasir.ezLibrary.domain.security.util.JwtTokenUtil;
import com.mhdjasir.ezLibrary.domain.user.ResetPasswordDTO;
import com.mhdjasir.ezLibrary.domain.user.UserService;
import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import com.mhdjasir.ezLibrary.exception.ApplicationCustomException;
import com.mhdjasir.ezLibrary.mail.MailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MailService mailService;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public String generatePassword() {
        byte[] randomBytes = new byte[12];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public ApplicationResponseDTO signUp(AuthRequestDTO authRequestDTO) {
        if (userRepository.findByEmail(authRequestDTO.getEmail()).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_EXIST", "Email already exist");
        }
        if (userRepository.findByMobile(authRequestDTO.getMobile()).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "MOBILE_ALREADY_EXIST", "Mobile already exist");
        }

        if (!authRequestDTO.getPassword().equals(authRequestDTO.getConfirmPassword())) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CONFIRM_PASSWORD_NOT_MATCHED", "Confirm password not matched");
        }

//        String generatePassword = generatePassword();
//        mailService.sendAccountCredentialMail("User Account Credentials", authRequestDTO.getEmail(), authRequestDTO.getName(), generatePassword);
        userRepository.save(
                User.builder()
                        .name(authRequestDTO.getName())
                        .email(authRequestDTO.getEmail())
//                        .password(passwordEncoder.encode(generatePassword))
                        .password(passwordEncoder.encode(authRequestDTO.getPassword()))
                        .mobile(authRequestDTO.getMobile())
//                        .status(false)
                        .status(true)
                        .delete(false)
                        .userRole(UserRole.USER)
                        .build()
        );
        return new ApplicationResponseDTO(HttpStatus.CREATED, "USER_REGISTERED_SUCCESSFULLY", "User registered successfully!");

    }

    public AuthResponseDTO login(LogInDTO logInDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(logInDTO.getEmail());
        if (optionalUser.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_EMAIL", "Invalid email");
        } else {
            User user = optionalUser.get();
            if (!passwordEncoder.matches(logInDTO.getPassword(), user.getPassword())) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_EMAIL_OR_PASSWORD", "Invalid email or password");
            }

            checkAccountStatus(user);

//            userRepository.save(user);

            String accessToken = jwtTokenUtil.generateAccessToken(user);
            String refreshToken = jwtTokenUtil.generateRefreshToken(user);
            return new AuthResponseDTO(HttpStatus.OK, "LOGIN_SUCCESS", "Login success", accessToken, refreshToken);
        }
    }

    public ApplicationResponseDTO resetPassword(ResetPasswordDTO resetPasswordDTO) {
        User user = userService.findByEmail(userService.getCurrentUser().getEmail());
        if (!passwordEncoder.matches(resetPasswordDTO.getOldPassword(), user.getPassword())) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_OLD_PASSWORD", "Invalid old password");
        } else if (!(resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmPassword()))) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CONFIRM_PASSWORD_DOES_NOT_MATCH", "Confirm password does not match");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        userRepository.save(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "USER_NEW_PASSWORD_UPDATED_SUCCESSFULLY", "User new password updated successfully!");
    }

    public AuthResponseDTO generateRefreshToken(String refreshToken) {
        if (jwtTokenUtil.validateToken(refreshToken)) {
            String email = jwtTokenUtil.getUsernameFromToken(refreshToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));
            String accessToken = jwtTokenUtil.generateAccessToken(user);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(user);
            String role = user.getUserRole().toString();
            return new AuthResponseDTO(HttpStatus.OK, "NEW_ACCESS_TOKEN_&_NEW_REFRESH_TOKEN", "New access & refresh token", accessToken, newRefreshToken);
        }
        throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", "Invalid refresh token");
    }

    public ApplicationResponseDTO forgotPassword(String email, HttpServletRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "EMAIL_NOT_FOUND", "Email not found")
        );

        checkAccountStatus(user);

        String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
        String resetPasswordLink = baseUrl + "/auth/reset-password/" + user.getId();

        mailService.sendForgotPasswordMail("Reset password", email, user.getName(), resetPasswordLink);

        return new ApplicationResponseDTO(HttpStatus.OK, "FORGOT_PASSWORD_SENT_SUCCESSFULLY", "Forgot password link sent to your email ");
    }

    public void checkAccountStatus(User user) {
        if (user.getDelete()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ACCOUNT_DELETED", "Account deleted");
        }

        if (!user.getStatus()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ACCOUNT_DISABLED", "Account disabled");
        }
    }

    public ApplicationResponseDTO resetForgotPassword(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found")
        );
        checkAccountStatus(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "VALID_RESET_PASSWORD_LINK", "Valid reset password link!");
    }

    public ApplicationResponseDTO resetForgotPassword(Long id, ResetForgotPasswordDTO resetForgotPasswordDTO) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found")
        );
        checkAccountStatus(user);

        if (!(resetForgotPasswordDTO.getNewPassword().equals(resetForgotPasswordDTO.getConfirmPassword()))) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CONFIRM_PASSWORD_DOES_NOT_MATCH", "Confirm password does not match");
        }
        user.setPassword(passwordEncoder.encode(resetForgotPasswordDTO.getNewPassword()));
        userRepository.save(user);
        return new ApplicationResponseDTO(HttpStatus.OK, "USER_NEW_PASSWORD_UPDATED_SUCCESSFULLY", "User new password updated successfully!");
    }
}
