package app.auth.repositories;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
public class ScheduledJob {
    private final VerificationCodesRepository verificationCodesRepository;

    @Scheduled(cron = "${verification.codes.refresh.cron}")
    public void deleteAllInvalidVerificationCodes() {
        verificationCodesRepository.deleteAllInvalidVerificationCodes();
    }
}