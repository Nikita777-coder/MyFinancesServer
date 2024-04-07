package app.auth.controllers;

import app.auth.dto.request.EmailVerificationRequest;
import app.auth.entities.VerificationCodeEntity;
import app.auth.servicies.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send_verification_code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam("email") String email) {
        emailService.sendVerificationCodeToEmail(email);

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerificationRequest emailVerificationRequest) {
        emailService.verifyEmail(emailVerificationRequest);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("is_email_exists")
    public ResponseEntity<Boolean> isEmailExists(@RequestParam("email") String email) {
        return new ResponseEntity<>(emailService.isEmailExists(email), HttpStatus.OK);
    }
}
