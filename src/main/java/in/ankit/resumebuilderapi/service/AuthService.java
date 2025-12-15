package in.ankit.resumebuilderapi.service;

import in.ankit.resumebuilderapi.document.User;
import in.ankit.resumebuilderapi.dto.AuthResponse;
import in.ankit.resumebuilderapi.dto.LoginRequest;
import in.ankit.resumebuilderapi.dto.RegisterRequest;
import in.ankit.resumebuilderapi.exception.EmailNotVerifiedException;
import in.ankit.resumebuilderapi.exception.ResourceExistsException;
import in.ankit.resumebuilderapi.repository.UserRepository;
import in.ankit.resumebuilderapi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    @Value("${app.base.url}")
    private String appbaseUrl;

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        log.info("Inside AuthService: register() {}", request);
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceExistsException("User Already Exists with this email");
        }

        User newUser = toUserDocument(request);

        userRepository.save(newUser);

        sendVerificationEmail(newUser);

        return toResponse(newUser);
    }

    private void sendVerificationEmail(User newUser) {
        log.info("Inside Auth Service - sendVerificationEmail(): {} ", newUser);
        try {
            String link = appbaseUrl + "api/auth/verify-email?token=" + newUser.getVerificationToken();
            String html = "<div style='font-family:sans-serif'>" +
                    "<h2>Verify your html </h2>" +
                    "<p>Hi " + newUser.getName() + ", Please confirm your email to activate your account.</p>" +
                    "<p><a href='" + link +
                    "'style='display:inline-block;padding:10px 16px; background: #6366f1; color:#fff; border-radius:6px; text-decoration:none'> Verify Email </a></p>"
                    +
                    "<p>Or copy this link: " + link + "</p>" +
                    "</div>";
            emailService.sendHtmlEmail(newUser.getEmail(), "Verify your email", html);
        } catch (Exception e) {
            log.info("Exception occured at sendVerificationEmail(): {}", e.getMessage());
            throw new RuntimeException("Failed to send verification Email: " + e.getMessage());
        }
    }

    public void verifyEmail(String token) {
        log.info("Inside Auth Service: verifyEmail(): {}", token);
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or Expired Verfication Token"));

        if (user.getVerificationExpires() != null && user.getVerificationExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification Token has Expired. Please request a new one");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpires(null);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User existingUser = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Username or Password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
            throw new UsernameNotFoundException("Invalid Username or Password");
        }

        if (!existingUser.isEmailVerified()) {
            sendVerificationEmail(existingUser);
            throw new EmailNotVerifiedException("Please verify your email");
        }

        String token = jwtUtil.generateToken(existingUser.getId());

        AuthResponse authResponse = toResponse(existingUser);
        authResponse.setToken(token);

        return authResponse;

    }

    private AuthResponse toResponse(User newUser) {
        return AuthResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .profileImageUrl(newUser.getProfileImageUrl())
                .emailVerified(newUser.isEmailVerified())
                .subscriptionPlan(newUser.getSubscriptionPlan())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .token(null) // Initialize token as null, it will be set later for login
                .build();
    }

    private User toUserDocument(RegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profileImageUrl(request.getProfileImageUrl())
                .subscriptionPlan("Basic")
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verificationExpires(LocalDateTime.now().plusHours(24))
                .build();
    }

    public void resendVerificationEmail(String email) {
        log.info("Inside Auth Service - reresendVerificationEmail(): {} ", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (user.isEmailVerified()) {
            throw new RuntimeException("Email is already verified.");
        }

        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationExpires(LocalDateTime.now().plusHours(24));
        userRepository.save(user);

        sendVerificationEmail(user);
    }

    public AuthResponse getProfile(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            return toResponse(user);
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            String email = userDetails.getUsername();
            User existingUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
            return toResponse(existingUser);
        } else if (principal instanceof String str) {
            User existingUser = userRepository.findByEmail(str)
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + str));
            return toResponse(existingUser);
        } else {
            throw new RuntimeException("Unsupported principal type: " + principal.getClass());
        }
    }

}
