package in.ankit.resumebuilderapi.service;

import in.ankit.resumebuilderapi.document.User;
import in.ankit.resumebuilderapi.dto.AuthResponse;
import in.ankit.resumebuilderapi.dto.RegisterRequest;
import in.ankit.resumebuilderapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    public AuthResponse register(RegisterRequest request){
        log.info("Inside AuthService: register() {}", request);
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("User Already Exists with this email");
        }

        User newUser = toUserDocument(request);

        userRepository.save(newUser);

        return  toResponse(newUser);
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
