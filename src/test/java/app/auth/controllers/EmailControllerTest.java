package app.auth.controllers;

import app.auth.dto.request.EmailVerificationRequest;
import app.auth.entities.VerificationCodeEntity;
import app.auth.repositories.VerificationCodesRepository;
import app.auth.servicies.EmailService;
import app.auth.servicies.VerificationCodesService;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.InvocationTargetException;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EmailControllerTest {
    @Mock
    private EmailService emailService;

    @Mock
    private VerificationCodesService verificationCodesService;

    @Mock
    private VerificationCodesRepository verificationCodesRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        verificationCodesService = new VerificationCodesService(verificationCodesRepository, passwordEncoder);
        emailService = new EmailService(javaMailSender, verificationCodesService, verificationCodesRepository);
    }

    private final List<String> testEmails = Arrays.asList("reta2224@jcnorris.com", "ckk0xg0320@waterisgone.com", "mflwk2b5sp@maili.fun");
    @Autowired
    private TestRestTemplate restTemplate;
    @Test
    @DisplayName("send verification code test; give first email from testEmails; must return 201 (CREATED) status code")
    void sendVerificationCode1() {
        ResponseEntity<String> response = restTemplate.postForEntity("/email/send_verification_code", testEmails.get(0), String.class);

        assertSame(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("send verification code test; give all emails from testEmails; must return true which indicate that messages were send to all emails")
    void sendVerificationCode2() {
        boolean isCreated = false;

        for (String email : testEmails) {
            ResponseEntity<String> response = restTemplate.postForEntity("/email/send_verification_code", email, String.class);
            isCreated = response.getStatusCode() == HttpStatus.CREATED;
        }

        assertTrue(isCreated);
    }

    @Test
    @DisplayName("send verification code test; send code to 1st email from testEmails two times; must return BAD_REQUEST status with message \"invalid request\" to 2nd request and CREATED to 1st")
    void sendVerificationCode3() {
        ResponseEntity<String> response1 = restTemplate.postForEntity("/email/send_verification_code", testEmails.get(0), String.class);
        ResponseEntity<String> response2 = restTemplate.postForEntity("/email/send_verification_code", testEmails.get(0), String.class);

        assertTrue(response1.getStatusCode() == HttpStatus.CREATED &&
                response2.getStatusCode() == HttpStatus.BAD_REQUEST &&
                Objects.equals(response2.getBody(), "invalid request"));
    }

    @Test
    @DisplayName("send verification code test; send code to 1st email from testEmails two times from 1 minute; must CREATED to all requests")
    void sendVerificationCode4() throws InterruptedException {
        ResponseEntity<String> response1 = restTemplate.postForEntity("/email/send_verification_code", testEmails.get(0), String.class);
        Thread.sleep(60_000);
        ResponseEntity<String> response2 = restTemplate.postForEntity("/email/send_verification_code", testEmails.get(0), String.class);

        assertTrue(response1.getStatusCode() == HttpStatus.CREATED &&
                response2.getStatusCode() == HttpStatus.CREATED);
    }

    @Test
    @DisplayName("send verification code test; send code to 1st email from testEmails 3 times from 1 minute; must CREATED to all requests")
    void sendVerificationCode5() throws InterruptedException {
        ResponseEntity<String> response1 = restTemplate.postForEntity("/email/send_verification_code", testEmails.get(0), String.class);
        Thread.sleep(60_000);
        ResponseEntity<String> response2 = restTemplate.postForEntity("/email/send_verification_code", testEmails.get(0), String.class);
        Thread.sleep(60_000);
        ResponseEntity<String> response3 = restTemplate.postForEntity("/email/send_verification_code", testEmails.get(0), String.class);

        assertTrue(response1.getStatusCode() == HttpStatus.CREATED &&
                response2.getStatusCode() == HttpStatus.CREATED);
    }

    @Test
    @DisplayName("verify code test; send verification code to 1st test email from testEmails, try to verify with code \"1234\"; must doesn't throw")
    void verifyEmailTest1() {
        LocalDateTime fixedDateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        List<VerificationCodeEntity> entities = Collections.singletonList(VerificationCodeEntity.
                builder()
                .email(testEmails.get(0))
                .verificationCode("1234")
                .expiredAt(fixedDateTime).build());

        when(verificationCodesService.addNewVerificationCode(new EmailVerificationRequest())).thenReturn(new VerificationCodeEntity());
        when(passwordEncoder.encode("1234")).thenReturn("1234");
        when(verificationCodesRepository.findByEmail(testEmails.get(0))).thenReturn(Optional.of(entities));
        emailService.sendVerificationCodeToEmail(testEmails.get(0));

        mockStatic(LocalDateTime.class);
        when(LocalDateTime.now()).thenReturn(fixedDateTime);

        assertDoesNotThrow(() -> emailService.verifyEmail(new EmailVerificationRequest(testEmails.get(0), "1234")));
    }

    @Test
    @DisplayName("verify code test; try verify code to 1st test email without sending code; must return BAD_REQUEST with message (\"there is no verification code send to email\")")
    void verifyEmailTest2() {
        ResponseEntity<String> response = restTemplate.exchange("/email/verify", HttpMethod.GET, new HttpEntity<>(new EmailVerificationRequest(testEmails.get(0), "1234")), String.class);

        assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST && Objects.equals(response.getBody(), "there is no verification code send to email"));
    }

    @Test
    @DisplayName("verify code test; send verification code to 1st test email from testEmails, mock verification password to \"5678\", try verify with code \"1234\"; must return IllegalArgumentException(\"invalid request!\")")
    void verifyEmailTest3() {
        LocalDateTime fixedDateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        List<VerificationCodeEntity> entities = Collections.singletonList(VerificationCodeEntity.
                builder()
                .email(testEmails.get(0))
                .verificationCode("5678")
                .expiredAt(fixedDateTime)
                .expiredAt(fixedDateTime).build());

        when(verificationCodesService.addNewVerificationCode(new EmailVerificationRequest())).thenReturn(new VerificationCodeEntity());
        when(passwordEncoder.encode("1234")).thenReturn("1234");
        when(verificationCodesRepository.findByEmail(testEmails.get(0))).thenReturn(Optional.of(entities));
        emailService.sendVerificationCodeToEmail(testEmails.get(0));

        mockStatic(LocalDateTime.class);
        when(LocalDateTime.now()).thenReturn(fixedDateTime);

        var response = assertThrows(IllegalArgumentException.class, () -> emailService.verifyEmail(new EmailVerificationRequest(testEmails.get(0), "1234")));
        assertEquals("invalid request!", response.getMessage());
    }

    @Test
    @DisplayName("verify code test; send verification code to 1st test email from testEmails, mock LocalDateTime one < LocalDateTime.now(), try verify with code \"1234\"; must return IllegalArgumentException(\"invalid request!\")")
    void verifyEmailTest4() {
        LocalDateTime fixedDateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        List<VerificationCodeEntity> entities = Collections.singletonList(VerificationCodeEntity.
                builder()
                .email(testEmails.get(0))
                .verificationCode("5678")
                .expiredAt(fixedDateTime.minusDays(1)).build());

        when(verificationCodesService.addNewVerificationCode(new EmailVerificationRequest())).thenReturn(new VerificationCodeEntity());
        when(passwordEncoder.encode("1234")).thenReturn("1234");
        when(verificationCodesRepository.findByEmail(testEmails.get(0))).thenReturn(Optional.of(entities));
        emailService.sendVerificationCodeToEmail(testEmails.get(0));

        mockStatic(LocalDateTime.class);
        when(LocalDateTime.now()).thenReturn(fixedDateTime);

        var response = assertThrows(IllegalArgumentException.class, () -> emailService.verifyEmail(new EmailVerificationRequest(testEmails.get(0), "1234")));
        assertEquals("invalid request!", response.getMessage());
    }
}