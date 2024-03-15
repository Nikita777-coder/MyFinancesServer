package app.auth.servicies;

import app.auth.dto.request.EmailVerificationRequest;
import app.auth.entities.VerificationCodeEntity;
import app.auth.repositories.VerificationCodesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationCodesService {
    @Value("${verification.code.expire.time}")
    private int timeOfValidVerificationCode;
    private final VerificationCodesRepository verificationCodesRepository;
    private final PasswordEncoder encoder;

    public VerificationCodeEntity addNewVerificationCode(EmailVerificationRequest emailVerificationRequest) {
        VerificationCodeEntity newVerificationCodeEntity = new VerificationCodeEntity();
        newVerificationCodeEntity.setEmail(emailVerificationRequest.getEmail());
        newVerificationCodeEntity.setVerificationCode(encoder.encode(emailVerificationRequest.getVerificationCode()));
        newVerificationCodeEntity.setExpiredAt(LocalDateTime.now().plusMinutes(timeOfValidVerificationCode));

        return verificationCodesRepository.save(newVerificationCodeEntity);
    }

    public void checkVerificationRequest(EmailVerificationRequest emailVerificationRequest) {
        List<VerificationCodeEntity> result = verificationCodesRepository.findAllByEmail(emailVerificationRequest.getEmail());

        if (result.isEmpty()) {
            throw new IllegalArgumentException("there is no verification code send to email");
        }

        VerificationCodeEntity resultEntity = result.get(result.size() - 1);

        if (LocalDateTime.now().isAfter(resultEntity.getExpiredAt())) {
            // log.warn("not correct date");
            throw new IllegalArgumentException("invalid request!");
        }

        if (!resultEntity.getVerificationCode().equals(encoder.encode(emailVerificationRequest.getVerificationCode()))) {
            // log.warn("not correct verification code");
            throw new IllegalArgumentException("invalid request!");
        }
    }
}
