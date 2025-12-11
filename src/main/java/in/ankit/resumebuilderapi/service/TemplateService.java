package in.ankit.resumebuilderapi.service;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import in.ankit.resumebuilderapi.dto.AuthResponse;
import in.ankit.resumebuilderapi.util.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService {

    private final AuthService authService;

    public Map<String, Object> getAllTemplates(Authentication authentication) {
        log.info("Inside TemplateService - getAllTemplates()");

        AuthResponse profile = authService.getProfile(authentication);

        List<String> allTemplates = Arrays.asList("template1", "template2", "template3", "template4", "template5");
        List<String> availableTemplates;

        Boolean isPremium = AppConstants.PREMIUM.equalsIgnoreCase(profile.getSubscriptionPlan());

        if (isPremium) {
            availableTemplates = allTemplates;
        } else {
            availableTemplates = List.of("template1");
        }

        Map<String, Object> restrictions = new HashMap<>();
        restrictions.put("availableTemplates", availableTemplates);
        restrictions.put("allTemplates", allTemplates);
        restrictions.put("subscriptionPlan", profile.getSubscriptionPlan());
        restrictions.put("isPremium", isPremium);

        return restrictions;
    }
}
