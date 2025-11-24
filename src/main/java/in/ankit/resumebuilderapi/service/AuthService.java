package in.ankit.resumebuilderapi.service;

import in.ankit.resumebuilderapi.document.User;
import in.ankit.resumebuilderapi.dto.AuthResponse;
import in.ankit.resumebuilderapi.dto.RegisterRequest;
import in.ankit.resumebuilderapi.exception.ResourceExistsException;
import in.ankit.resumebuilderapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
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

    public AuthResponse register(RegisterRequest request){
        log.info("Inside AuthService: register() {}", request);
        if (userRepository.existsByEmail(request.getEmail())){
            throw new ResourceExistsException("User Already Exists with this email");
        }

        User newUser = toUserDocument(request);

        userRepository.save(newUser);

        sendVerificationEmail(newUser);

        return  toResponse(newUser);
    }

    private void sendVerificationEmail(User newUser) {
        log.info("Inside Auth Service - sendVerificationEmail(): {} ", newUser);
        try{
            String link = appbaseUrl + "api/auth/verify-email?token=" + newUser.getVerificationToken();
            String html = "<div style='font-family:sans-serif'>" +
                    "<h2>Verify your html </h2>" +
                    "<p>Hi " + newUser.getName() + ", Please confirm your email to activate your account.</p>" +
                    "<p><a href='" + link +
                    "'style='display:inline-block;padding:10px 16px; background: #6366f1; color:#fff; border-radius:6px; text-decoration:none'> Verify Email </a></p>" +
                    "<p>Or copy this link: " + link + "</p>" +
                    "</div>";
            emailService.sendHtmlEmail(newUser.getEmail(),"Verify your email",html);
        } catch (Exception e) {
            log.info("Exception occured at sendVerificationEmail(): {}", e.getMessage());
            throw new RuntimeException("Failed to send verification Email: " + e.getMessage());
        }
    }

    public void verifyEmail(String token){
        log.info("Inside Auth Service: verifyEmail(): {}",token );
        User user = userRepository.findByVerificationToken(token)
        .orElseThrow(() -> new RuntimeException("Invalid or Expired Verfication Token"));
        
        if(user.getVerificationExpires() != null && user.getVerificationExpires().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Verification Token has Expired. Please request a new one");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpires(null);
        userRepository.save(user);
    }

    private AuthResponse toResponse(User newUser){
    return AuthResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .profileImageUrl(newUser.getProfileImageUrl())
                .emailVerified(newUser.isEmailVerified())
                .subscriptionPlan(newUser.getSubscriptionPlan())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .build();
    }

    private User toUserDocument(RegisterRequest request){
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .profileImageUrl(request.getProfileImageUrl())
                .subscriptionPlan("Basic")
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verificationExpires(LocalDateTime.now().plusHours(24))
                .build();
    }
}
