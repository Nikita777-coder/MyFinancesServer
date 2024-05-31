package app.auth.servicies;

import app.auth.dto.request.EmailVerificationRequest;
import app.auth.entities.VerificationCodeEntity;

public interface VerificationCodesService {

    public VerificationCodeEntity addNewVerificationCode(EmailVerificationRequest emailVerificationRequest);

    public void checkVerificationRequest(EmailVerificationRequest emailVerificationRequest);
}
