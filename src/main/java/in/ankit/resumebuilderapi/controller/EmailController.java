package in.ankit.resumebuilderapi.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.ankit.resumebuilderapi.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @PostMapping(value = "/send-resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> sendResumeByEmail(@RequestPart("recipientEmail") String recipientEmail,
            @RequestPart(value = "subject", required = false) String subject,
            @RequestPart(value = "message", required = false) String message,
            @RequestPart("pdfFile") MultipartFile pdfFile,
            Authentication authentication) throws IOException, MessagingException {

        log.info("Inside EmailController - sendResumeByEmail(): {} {} {}", recipientEmail, subject, message);
        Map<String, Object> response = new HashMap<>();
        if (Objects.isNull(recipientEmail)
                || Objects.isNull(pdfFile)) {
            response.put("Message", "Invalid email details");
            response.put("Success", "False");
            return ResponseEntity.badRequest().body(response);
        }

        byte[] pdfData = pdfFile.getBytes();
        String originalFileName = pdfFile.getOriginalFilename();
        String fileName = Objects.nonNull(originalFileName) ? originalFileName : "resume.pdf";

        String emailSubject = Objects.nonNull(subject) && !subject.isEmpty() ? subject
                : "Your Resume from Resume Builder App";
        String emailContent = Objects.nonNull(message) && !message.isEmpty() ? message
                : "Please find attached your resume.";

        emailService.sendEmailWithAttachment(recipientEmail, emailSubject, emailContent, pdfData, fileName);

        response.put("Success", true);
        response.put("Message", "Email sent successfully");
        return ResponseEntity.ok(response);
    }
}
