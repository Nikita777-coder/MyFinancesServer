package app.auth.servicies;

import app.auth.dto.request.EmailVerificationRequest;


public interface EmailService {

    public void verifyEmail(EmailVerificationRequest emailVerificationRequest);

    public void sendVerificationCodeToEmail(String email);

    public boolean isEmailExists(String email);
}
