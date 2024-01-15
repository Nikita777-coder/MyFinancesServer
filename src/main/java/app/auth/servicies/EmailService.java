package app.auth.servicies;

import app.auth.dto.request.EmailVerificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
//@Slf4j
public class EmailService {
    @Value("${server.email}")
    private String serverMail;

    @Value("${verification.code.length}")
    private int verificationCodeLength;
    private final JavaMailSender mailSender;
    private final VerificationCodesService verificationCodesService;
    public void verifyEmail(EmailVerificationRequest emailVerificationRequest) {
        verificationCodesService.checkVerificationRequest(emailVerificationRequest);
    }
    public void sendVerificationCodeToEmail(String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String generatedVerificationPassword = generateSafeVerificationCode();
            helper.setText(generatedVerificationPassword);

            helper.setTo(email);
            helper.setSubject("Confirm your email");
            helper.setFrom(serverMail);
            mailSender.send(mimeMessage);

            verificationCodesService.addNewVerificationCode(new EmailVerificationRequest(email, generatedVerificationPassword));
        } catch (MessagingException e) {
            //log.error("Failed to send email for: " + email + "\n" + e);
            throw new IllegalArgumentException("Failed to send email for: " + email);
        }
    }
    private String generateSafeVerificationCode() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[verificationCodeLength];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }
}
