package in.ankit.resumebuilderapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${app.mail.from}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        log.info("Inside Email Service - sendHtmlEmail(): {} {} {}", to, subject, htmlContent);
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
        mimeMessageHelper.setFrom(fromEmail);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachmentData,
            String attachmentFilename) throws MessagingException {
        log.info("Inside Email Service - sendEmailWithAttachment(): {} {} {}", to, subject, body);
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
        mimeMessageHelper.setFrom(fromEmail);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body);

        mimeMessageHelper.addAttachment(attachmentFilename, new ByteArrayResource(attachmentData));

        mailSender.send(message);
    }
}
