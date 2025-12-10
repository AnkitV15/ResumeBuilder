package in.ankit.resumebuilderapi.controller;

import in.ankit.resumebuilderapi.dto.AuthResponse;
import in.ankit.resumebuilderapi.dto.LoginRequest;
import in.ankit.resumebuilderapi.dto.RegisterRequest;
import in.ankit.resumebuilderapi.service.AuthService;
import in.ankit.resumebuilderapi.service.FileUploadService;
import in.ankit.resumebuilderapi.util.AppConstants;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.AUTH_CONTROLLER)
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final FileUploadService fileUploadService;

    @PostMapping(AppConstants.REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Inside AuthController - register() : {}", request);
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(AppConstants.VERIFY_EMAIL)
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        log.info("Inside AuthController - verifyEmail() : {}", token);
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("Message", "Email Verified Successfully"));
    }

    @PostMapping(AppConstants.UPLOAD_PROFILE)
    public ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile fMultipart) {
        log.info("Inside AuthController - uploadImage()");
        Map<String, String> response;
        try {
            response = fileUploadService.uploadSingleImage(fMultipart);
        } catch (Exception e) {
            throw new RuntimeException("File Upload failed!");
            // return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(AppConstants.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Inside AuthController - loginUser() : {}", loginRequest);
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(AppConstants.RESEND_VERIFY_EMAIL)
    public ResponseEntity<?> resendVerifyEmail(@RequestBody Map<String, String> body) {
        log.info("Inside AuthController - resendVerifyEmail() : {}", body);
        String email = body.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("Error", "Email is required"));
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            return ResponseEntity.badRequest().body(Map.of("Error", "Invalid email format"));
        }
        authService.resendVerificationEmail(email);
        return ResponseEntity.ok(Map.of("Success", true, "Message", "Verification Email Sent Successfully"));
    }

    @GetMapping(AppConstants.PROFILE)
    public ResponseEntity<?> getProfile(Authentication authentication) {
        log.info("Inside AuthController - getProfile()");

        AuthResponse authResponse = authService.getProfile(authentication);

        return ResponseEntity.ok(authResponse);
    }

    // @PostMapping(AppConstants.LOGOUT)
    // public ResponseEntity<?> logout() {
    // log.info("Inside AuthController - logoutUser()");
    // authService.logout();
    // return ResponseEntity.ok(Map.of("Message", "Logged out successfully"));
    // }
}
