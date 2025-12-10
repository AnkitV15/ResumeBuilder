package in.ankit.resumebuilderapi.controller;

import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.ankit.resumebuilderapi.document.Resume;
import in.ankit.resumebuilderapi.dto.CreateResumeRequest;
import in.ankit.resumebuilderapi.service.FileUploadService;
import in.ankit.resumebuilderapi.service.ResumeService;
import in.ankit.resumebuilderapi.util.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(AppConstants.RESUME)
@RequiredArgsConstructor
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;
    private final FileUploadService fileUploadService;

    @PostMapping("/create")
    public ResponseEntity<?> createResume(@Valid @RequestBody CreateResumeRequest resumeRequest,
            Authentication authentication) {
        log.info("Inside ResumeController - createResume()");
        Resume createdResume = resumeService.createResume(resumeRequest, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResume);
    }

    @GetMapping(AppConstants.GET_USER_RESUMES)
    public ResponseEntity<?> getUserResumes(Authentication authentication) {
        log.info("Inside ResumeController - getUserResumes()");
        List<Resume> resumes = resumeService.getUserResumes(authentication);
        return ResponseEntity.ok(resumes);
    }

    @GetMapping(AppConstants.ID)
    public ResponseEntity<?> getResumeById(@PathVariable String id, Authentication authentication) {
        log.info("Inside ResumeController - getResumeById()");
        Resume resume = resumeService.getResumeById(id, authentication);
        return ResponseEntity.ok(resume);
    }

    @PutMapping(AppConstants.ID)
    public ResponseEntity<?> updateResume(@PathVariable String id, @RequestBody Resume updatedData,
            Authentication authentication) {
        log.info("Inside ResumeController - updateResume()");
        Resume resume = resumeService.updateResume(id, updatedData, authentication);
        return ResponseEntity.ok(resume);
    }

    @PutMapping(AppConstants.UPLOAD_IMAGES)
    public ResponseEntity<?> uploadResumeImages(@PathVariable String id,
            @RequestPart(value = "thumbnail", required = true) MultipartFile thumbnail,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            Authentication authentication) {
        log.info("Inside ResumeController - uploadResumeImages()");
        Map<String, String> responseMap = fileUploadService.uploadResumeImages(id, thumbnail, profileImage,
                authentication);
        return ResponseEntity.ok(responseMap);
    }

    @DeleteMapping(AppConstants.ID)
    public ResponseEntity<?> deleteResumes(@PathVariable String id, Authentication authentication) {
        log.info("Inside ResumeController - deleteResumes()");
        resumeService.deleteResume(id, authentication);
        return ResponseEntity.ok(Map.of("message", "Resume deleted successfully") );
    }
}
