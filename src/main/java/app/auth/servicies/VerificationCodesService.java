package app.auth.servicies;

import app.auth.dto.request.EmailVerificationRequest;
import app.auth.entities.VerificationCodeEntity;
import app.auth.repositories.VerificationCodesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationCodesService {
    @Value("${verification.code.expire.time}")
    private int timeOfValidVerificationCode;
    private final VerificationCodesRepository verificationCodesRepository;
    private final PasswordEncoder encoder;

    public VerificationCodeEntity addNewVerificationCode(EmailVerificationRequest emailVerificationRequest) {
        if (verificationCodesRepository.findByEmail(emailVerificationRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("verification code has already send to email");
        }

        VerificationCodeEntity newVerificationCodeEntity = new VerificationCodeEntity();
        newVerificationCodeEntity.setEmail(emailVerificationRequest.getEmail());
        newVerificationCodeEntity.setVerificationCode(encoder.encode(emailVerificationRequest.getVerificationCode()));
        newVerificationCodeEntity.setExpiredAt(LocalDateTime.now().plusMinutes(timeOfValidVerificationCode));

        return verificationCodesRepository.save(newVerificationCodeEntity);
    }

    public void checkVerificationRequest(EmailVerificationRequest emailVerificationRequest) {
        Optional<VerificationCodeEntity> result = verificationCodesRepository.findByEmail(emailVerificationRequest.getEmail());

        if (result.isEmpty()) {
            throw new IllegalArgumentException("there is no verification code send to email");
        }

        VerificationCodeEntity resultEntity = result.get();

        if (resultEntity.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("time of verification code is expired");
        }

        if (!resultEntity.getVerificationCode().equals(encoder.encode(emailVerificationRequest.getVerificationCode()))) {
            throw new IllegalArgumentException("not correct verification code");
        }
    }
}
