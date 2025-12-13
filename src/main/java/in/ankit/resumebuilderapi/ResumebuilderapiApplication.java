package in.ankit.resumebuilderapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration.class
})
public class ResumebuilderapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumebuilderapiApplication.class, args);
    }

}
