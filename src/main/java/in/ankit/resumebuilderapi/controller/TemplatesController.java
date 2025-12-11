package in.ankit.resumebuilderapi.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.ankit.resumebuilderapi.service.TemplateService;
import in.ankit.resumebuilderapi.util.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(AppConstants.TEMPLATES)
@RequiredArgsConstructor
@Slf4j
public class TemplatesController {

    private final TemplateService templateService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllTemplates(Authentication authentication) {
        log.info("Inside TemplatesController - getAllTemplates()");
        Map<String, Object> templates = templateService.getAllTemplates(authentication);
        return ResponseEntity.ok(templates);
    }
}
