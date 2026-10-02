package com.tung.inventory.service;

import com.tung.inventory.dto.request.LoginRequest;
import com.tung.inventory.dto.request.RegisterRequest;
import com.tung.inventory.dto.response.AuthResponse;
import com.tung.inventory.entity.User;
import com.tung.inventory.exception.BusinessException;
import com.tung.inventory.repository.UserRepository;
import com.tung.inventory.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .fullName("Test User")
                .role(User.Role.OPERATOR)
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with valid credentials")
        void shouldLoginSuccessfully() {
            LoginRequest request = LoginRequest.builder()
                    .username("testuser")
                    .password("password123")
                    .build();

            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwt-token-123");

            AuthResponse response = authService.login(request);

            assertThat(response).isNotNull();
            assertThat(response.getToken()).isEqualTo("jwt-token-123");
            assertThat(response.getTokenType()).isEqualTo("Bearer");
            assertThat(response.getUser().getUsername()).isEqualTo("testuser");
            assertThat(response.getUser().getRole()).isEqualTo("OPERATOR");

            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtTokenProvider).generateToken(authentication);
        }

        @Test
        @DisplayName("Should throw exception with invalid credentials")
        void shouldThrowExceptionWithInvalidCredentials() {
            LoginRequest request = LoginRequest.builder()
                    .username("testuser")
                    .password("wrongpassword")
                    .build();

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            assertThatThrownBy(() -> authService.login(request))
                    .isInstanceOf(BadCredentialsException.class);
        }
    }

    @Nested
    @DisplayName("Register Tests")
    class RegisterTests {

        @Test
        @DisplayName("Should register successfully with valid data")
        void shouldRegisterSuccessfully() {
            RegisterRequest request = RegisterRequest.builder()
                    .username("newuser")
                    .password("password123")
                    .email("newuser@example.com")
                    .fullName("New User")
                    .build();

            when(userRepository.existsByUsername("newuser")).thenReturn(false);
            when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
            when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(2L);
                return user;
            });
            when(jwtTokenProvider.generateTokenFromUsername("newuser")).thenReturn("jwt-token-new");

            AuthResponse response = authService.register(request);

            assertThat(response).isNotNull();
            assertThat(response.getToken()).isEqualTo("jwt-token-new");
            assertThat(response.getUser().getUsername()).isEqualTo("newuser");
            assertThat(response.getUser().getEmail()).isEqualTo("newuser@example.com");

            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw exception when username already exists")
        void shouldThrowExceptionWhenUsernameExists() {
            RegisterRequest request = RegisterRequest.builder()
                    .username("existinguser")
                    .password("password123")
                    .email("new@example.com")
                    .fullName("New User")
                    .build();

            when(userRepository.existsByUsername("existinguser")).thenReturn(true);

            assertThatThrownBy(() -> authService.register(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Username");
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailExists() {
            RegisterRequest request = RegisterRequest.builder()
                    .username("newuser")
                    .password("password123")
                    .email("existing@example.com")
                    .fullName("New User")
                    .build();

            when(userRepository.existsByUsername("newuser")).thenReturn(false);
            when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

            assertThatThrownBy(() -> authService.register(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Email");
        }
    }
}
