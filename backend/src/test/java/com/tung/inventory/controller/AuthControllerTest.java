package com.tung.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tung.inventory.dto.request.LoginRequest;
import com.tung.inventory.dto.response.AuthResponse;
import com.tung.inventory.dto.response.ApiResponse;
import com.tung.inventory.security.JwtTokenProvider;
import com.tung.inventory.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("POST /api/v1/auth/login - Should return token on successful login")
    void shouldReturnTokenOnSuccessfulLogin() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .username("admin")
                .password("admin123")
                .build();

        AuthResponse response = AuthResponse.builder()
                .token("jwt-token-123")
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(AuthResponse.UserResponse.builder()
                        .id(1L)
                        .username("admin")
                        .email("admin@test.com")
                        .fullName("Admin User")
                        .role("ADMIN")
                        .build())
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.user.username").value("admin"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should return 400 for empty username")
    void shouldReturn400ForEmptyUsername() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .username("")
                .password("password")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should return 201 on successful registration")
    void shouldReturn201OnSuccessfulRegistration() throws Exception {
        com.tung.inventory.dto.request.RegisterRequest request =
            com.tung.inventory.dto.request.RegisterRequest.builder()
                .username("newuser")
                .password("password123")
                .email("new@example.com")
                .fullName("New User")
                .build();

        AuthResponse response = AuthResponse.builder()
                .token("jwt-token-new")
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(AuthResponse.UserResponse.builder()
                        .id(2L)
                        .username("newuser")
                        .email("new@example.com")
                        .fullName("New User")
                        .role("OPERATOR")
                        .build())
                .build();

        when(authService.register(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt-token-new"));
    }
}
