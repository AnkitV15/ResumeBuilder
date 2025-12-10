package in.ankit.resumebuilderapi.service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import in.ankit.resumebuilderapi.document.Resume;
import in.ankit.resumebuilderapi.document.Resume.ProfileInfo;
import in.ankit.resumebuilderapi.dto.AuthResponse;
import in.ankit.resumebuilderapi.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private final Cloudinary cloudinary;
    private final AuthService authService;
    private final ResumeRepository resumeRepository;

    public Map<String, String> uploadSingleImage(MultipartFile file) throws IOException {
        @SuppressWarnings("unchecked")
        Map<String, Object> imageUploadResult = (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "image"));
        log.info("Inside FileUploadService - uploadSingleImage() {}", imageUploadResult.get("secure_url").toString());
        return Map.of("imageUrl", imageUploadResult.get("secure_url").toString());
    }

    public Map<String, String> uploadResumeImages(String resumeId, MultipartFile thumbnail, MultipartFile profileImage,
            Authentication authentication) {
        AuthResponse response = authService.getProfile(authentication);

        Resume existingResume = resumeRepository.findByUserIdAndId(response.getId(), resumeId).orElseThrow(() -> {
            return new RuntimeException("Resume not found");
        });

        try {
            Map<String, String> resultMap = uploadSingleImage(thumbnail);
            String thumbnailUrl = resultMap.get("imageUrl");

            existingResume.setThumbnailLink(thumbnailUrl);

            String profileImageUrl = null;

            if (Objects.isNull(existingResume.getProfileInfo())) {
                existingResume.setProfileInfo(new ProfileInfo());
            }

            if (profileImage != null && !profileImage.isEmpty()) {
                Map<String, String> profileImageResultMap = uploadSingleImage(profileImage);
                profileImageUrl = profileImageResultMap.get("imageUrl");
                existingResume.getProfileInfo().setProfilePreviewUrl(profileImageUrl);
            }

            resumeRepository.save(existingResume);

            if (profileImage != null && !profileImage.isEmpty()) {
                profileImageUrl = existingResume.getProfileInfo().getProfilePreviewUrl();
            }
            if (profileImageUrl != null) {
                return Map.of("thumbnailUrl", thumbnailUrl, "profileImageUrl", profileImageUrl);
            } else {
                return Map.of("thumbnailUrl", thumbnailUrl);
            }
        } catch (IOException e) {
            log.error("Error uploading images for resume id {}: {}", resumeId, e.getMessage());
            throw new RuntimeException("Failed to upload images");
        }
    }
}
