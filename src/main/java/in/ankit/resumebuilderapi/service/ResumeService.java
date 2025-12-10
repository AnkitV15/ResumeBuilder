package in.ankit.resumebuilderapi.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import in.ankit.resumebuilderapi.document.Resume;
import in.ankit.resumebuilderapi.dto.AuthResponse;
import in.ankit.resumebuilderapi.dto.CreateResumeRequest;
import in.ankit.resumebuilderapi.repository.ResumeRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final AuthService authService;

    private final ResumeRepository resumeRepository;

    public Resume createResume(CreateResumeRequest resumeRequest, Authentication authentication) {
        log.info("Inside ResumeService - createResume()");

        Resume resume = new Resume();

        AuthResponse profile = authService.getProfile(authentication);

        resume.setUserId(profile.getId());
        resume.setTitle(resumeRequest.getTitle());

        setDefaultResumeData(resume);

        return resumeRepository.save(resume);
    }

    private void setDefaultResumeData(Resume newResume) {
        log.info("Inside ResumeService - setDefaultResumeData()");
        newResume.setProfileInfo(new Resume().getProfileInfo());
        newResume.setContactInfo(new Resume().getContactInfo());
        newResume.setWorkExperiences(new ArrayList<>());
        newResume.setEducations(new ArrayList<>());
        newResume.setSkills(new ArrayList<>());
        newResume.setProjects(new ArrayList<>());
        newResume.setLanguages(new ArrayList<>());
        newResume.setCertifications(new ArrayList<>());
        newResume.setInterests(new ArrayList<>());
    }

    public List<Resume> getUserResumes(Authentication authentication) {
        log.info("Inside ResumeService - getUserResumes()");

        AuthResponse profile = authService.getProfile(authentication);

        return resumeRepository.findByUserIdOrderByUpdatedAtDesc(profile.getId());
    }

    public Resume getResumeById(String id, Authentication authentication) {
        log.info("Inside ResumeService - getResumeById()");
        AuthResponse profile = authService.getProfile(authentication);
        return resumeRepository.findByUserIdAndId(profile.getId(), id)
                .orElseThrow(() -> new RuntimeException("Resume not found with id: " + id));
    }

    public Resume updateResume(String id, Resume updatedData, Authentication authentication) {
        log.info("Inside ResumeService - updateResume()");
        AuthResponse profile = authService.getProfile(authentication);
        Resume existingResume = resumeRepository.findByUserIdAndId(profile.getId(), id)
                .orElseThrow(() -> new RuntimeException("Resume not found with id: " + id));

        existingResume.setTitle(updatedData.getTitle());
        existingResume.setThumbnailLink(updatedData.getThumbnailLink());
        existingResume.setTemplates(updatedData.getTemplates());
        existingResume.setProfileInfo(updatedData.getProfileInfo());
        existingResume.setContactInfo(updatedData.getContactInfo());
        existingResume.setWorkExperiences(updatedData.getWorkExperiences());
        existingResume.setEducations(updatedData.getEducations());
        existingResume.setSkills(updatedData.getSkills());
        existingResume.setProjects(updatedData.getProjects());
        existingResume.setLanguages(updatedData.getLanguages());
        existingResume.setCertifications(updatedData.getCertifications());
        existingResume.setInterests(updatedData.getInterests());

        return resumeRepository.save(existingResume);
    }

    public Resume deleteResume(String id, Authentication authentication) {
        log.info("Inside ResumeService - deleteResume()");
        AuthResponse profile = authService.getProfile(authentication);
        Resume existingResume = resumeRepository.findByUserIdAndId(profile.getId(), id)
                .orElseThrow(() -> new RuntimeException("Resume not found with id: " + id));

        resumeRepository.delete(existingResume);
        return existingResume;
    }
}
