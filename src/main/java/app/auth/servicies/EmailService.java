package app.auth.servicies;

import app.auth.dto.request.EmailVerificationRequest;
import app.auth.entities.VerificationCodeEntity;
import app.auth.repositories.VerificationCodesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${server.email}")
    private String serverMail;

    @Value("${verification.code.length}")
    private int verificationCodeLength;
    private final JavaMailSender mailSender;
    private final VerificationCodesService verificationCodesService;
    private final VerificationCodesRepository verificationCodesRepository;
    public void verifyEmail(EmailVerificationRequest emailVerificationRequest) {
        verificationCodesService.checkVerificationRequest(emailVerificationRequest);
    }
    public List<VerificationCodeEntity> getVerificationCodes() {
        return verificationCodesRepository.findAll();
    }
    public void sendVerificationCodeToEmail(String email) {
        var result = verificationCodesRepository.findAllByEmail(email);

        if (!result.isEmpty() &&
                result.get(result.size() - 1).getExpiredAt().isAfter(LocalDateTime.now())) {
            // log.warn("verification code has already send to email %s", email);
            throw new IllegalArgumentException("invalid request");
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(serverMail);
        mailMessage.setTo(email);
        mailMessage.setSubject("MyFinances, verification code");
        String generatedVerificationPassword = generateSafeVerificationCode();
        mailMessage.setText(String.format("Your verification code - %s", generatedVerificationPassword));

        mailSender.send(mailMessage);
        verificationCodesService.addNewVerificationCode(new EmailVerificationRequest(email, generatedVerificationPassword));
    }
    private String generateSafeVerificationCode() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[verificationCodeLength];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes).substring(0, verificationCodeLength);
    }
}
