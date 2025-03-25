package com.mhdjasir.ezLibrary.domain.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhdjasir.ezLibrary.config.CustomErrorResponse;
import com.mhdjasir.ezLibrary.domain.security.entity.UserRole;
import com.mhdjasir.ezLibrary.domain.security.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private static final String ADMIN_URL = "/api/v1/admin/**";
    private static final String USER_URL = "/api/v1/user/**";
    private static final String BOOK_URL = "/api/v1/book/**";
    private static final String BOOK_GET_URL = "/api/v1/book/get/**";
    private static final String CATEGORY_URL = "/api/v1/category/**";
    private static final String WISHLIST_URL = "/api/v1/wishlist/**";
    private static final String CART_URL = "/api/v1/cart/**";
    private static final String ORDER_URL = "/api/v1/order/**";
    private static final String RATING_URL = "/api/v1/rating/**";
    private static final String REVIEW_URL = "/api/v1/review/**";


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headerConfig -> {
            headerConfig.xssProtection(xXssConfig -> xXssConfig.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED));
            headerConfig.contentSecurityPolicy(contentSecurityPolicyConfig -> contentSecurityPolicyConfig.policyDirectives("default-src 'self'"));
        });
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()

                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/sign-up").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/token/refresh/{refresh-token}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/forgot-password/{email}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/auth/reset-password/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/reset-password/{id}").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/v1/images/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/images/**").permitAll()

                .requestMatchers(HttpMethod.GET, ADMIN_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.POST, ADMIN_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.PUT, ADMIN_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.DELETE, ADMIN_URL).hasRole(String.valueOf(UserRole.ADMIN))

//                .requestMatchers(HttpMethod.GET, USER_URL).permitAll()
                .requestMatchers(HttpMethod.GET, USER_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.POST, USER_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, USER_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.DELETE, USER_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))

                .requestMatchers(HttpMethod.GET, BOOK_GET_URL).permitAll()
                .requestMatchers(HttpMethod.POST, BOOK_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, BOOK_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.DELETE, BOOK_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))

                //.requestMatchers(HttpMethod.GET, CATEGORY_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.GET, CATEGORY_URL).permitAll()
                .requestMatchers(HttpMethod.POST, CATEGORY_URL).hasRole(String.valueOf(UserRole.ADMIN))
                .requestMatchers(HttpMethod.PUT, CATEGORY_URL).hasRole(String.valueOf(UserRole.ADMIN))

                .requestMatchers(HttpMethod.GET, WISHLIST_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.POST, WISHLIST_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, WISHLIST_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.DELETE, WISHLIST_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))

                .requestMatchers(HttpMethod.GET, CART_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.POST, CART_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, CART_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.DELETE, CART_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))

                .requestMatchers(HttpMethod.GET, ORDER_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.POST, ORDER_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, ORDER_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.DELETE, ORDER_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))

                .requestMatchers(HttpMethod.GET, RATING_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.POST, RATING_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, RATING_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.DELETE, RATING_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))

                .requestMatchers(HttpMethod.GET, REVIEW_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.POST, REVIEW_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.PUT, REVIEW_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))
                .requestMatchers(HttpMethod.DELETE, REVIEW_URL).hasAnyRole(String.valueOf(UserRole.ADMIN), String.valueOf(UserRole.USER))

                .anyRequest().authenticated()
        );

        http.addFilterAfter(jwtTokenFilter, CorsFilter.class);
        http.exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer.authenticationEntryPoint((request, response, authException) -> {
//                        throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Invalid token");
//                        throw new ApplicationCustomException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", authException.getMessage());
                        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.FORBIDDEN.value(), "ApplicationCustomException", "Resource not found");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
                        return;
                    });
                    exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
//                        throw new ApplicationCustomException(HttpStatus.FORBIDDEN, "FORBIDDEN", accessDeniedException.getMessage());
//                        throw new ApplicationCustomException(HttpStatus.FORBIDDEN, "FORBIDDEN", "Unauthorized access");
                        CustomErrorResponse errorResponse = new CustomErrorResponse(HttpStatus.FORBIDDEN.value(), "ApplicationCustomException", "Unauthorized access");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
                        return;
                    });
                }
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
