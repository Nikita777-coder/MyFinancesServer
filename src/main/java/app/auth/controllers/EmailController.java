package app.auth.controllers;

import app.auth.dto.request.EmailVerificationRequest;
import app.auth.servicies.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send_verification_code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody String email) {
        emailService.sendVerificationCodeToEmail(email);

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerificationRequest emailVerificationRequest) {
        emailService.verifyEmail(emailVerificationRequest);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
