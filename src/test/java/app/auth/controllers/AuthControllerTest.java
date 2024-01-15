package app.auth.controllers;

import app.auth.dto.UserOutData;
import app.auth.dto.request.SignInRequest;
import app.auth.dto.request.SignUpRequest;
import app.auth.dto.request.UpdateUserDto;
import app.auth.entities.user.UserEntity;
import app.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
//@AutoConfigureTestDatabase(replace = NONE)
class AuthControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private void signUpUser(String email, String password) {
        SignUpRequest signUpRequest = new SignUpRequest(email, password);
        restTemplate.postForEntity("/auth/signup", signUpRequest, UserOutData.class);
    }

    @AfterEach
    void cleanUsers() {
        restTemplate.delete("/user/delete_all");
    }

    @Test
    @DisplayName("sign up test; give SignUpRequest(some@mail.ru; password01); must return UserDetails(some@mail.ru; encoded_password)")
    void createTest1() {
        SignUpRequest input = new SignUpRequest("some@mail.ru", "password01");
        ResponseEntity<UserOutData> response = restTemplate.postForEntity("/auth/signup", input, UserOutData.class);
        UserOutData responseUserDetails = response.getBody();
        assertTrue(response.getStatusCode() == HttpStatus.CREATED &&
                Objects.requireNonNull(responseUserDetails).getEmail().equals("some@mail.ru") &&
                passwordEncoder.matches("password01", Objects.requireNonNull(responseUserDetails).getPassword())
        );
    }

    @Test
    @DisplayName("sign up test; give SignUpRequest(somemail.ru; password01); must return BAD_REQUEST code")
    void createTest2() {
        SignUpRequest input = new SignUpRequest("somemail.ru", "password01");
        ResponseEntity<UserOutData> response = restTemplate.postForEntity("/auth/signup", input, UserOutData.class);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("sign up test; give SignUpRequest(some@mail.ru; password01) and SignUpRequest(some@mail.ru; password01); must return BAD_REQUEST to second request")
    void createTest3() {
        SignUpRequest input1 = new SignUpRequest("some@mail.ru", "password01");
        SignUpRequest input2 = new SignUpRequest("some@mail.ru", "password01");
        ResponseEntity<UserOutData> response1 = restTemplate.postForEntity("/auth/signup", input1, UserOutData.class);
        ResponseEntity<UserOutData> response2 = restTemplate.postForEntity("/auth/signup", input2, UserOutData.class);
        assertTrue(response1.getStatusCode() == HttpStatus.CREATED && response2.getStatusCode() == HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("sign in test; give SignInRequest(some@mail.ru; password01) and sign up this user before; must return OK code")
    void enterTest1() {
        signUpUser("some@mail.ru","password01");
        UpdateUserDto updateUserDto = UpdateUserDto.builder().isActive(false).requestEmail("some@mail.ru").build();
        restTemplate.patchForObject("/user/update_profile", updateUserDto, UpdateUserDto.class);
        SignInRequest input = new SignInRequest();
        input.setEmail("some@mail.ru");
        input.setPassword("password01");

        ResponseEntity<String> response = restTemplate.postForEntity("/auth/signin", input, String.class);
        assertSame(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("sign in test; give SignInRequest(some@mail.ru; password01) and sign up this user before. " +
            "Then try to enter to account when it is active; second request must return BAD_REQUEST code and first OK code")
    void enterTest2() {
        signUpUser("some@mail.ru","password01");
        SignInRequest input = new SignInRequest();
        input.setEmail("some@mail.ru");
        input.setPassword("password01");

        ResponseEntity<String> response = restTemplate.postForEntity("/auth/signin", input, String.class);
        ResponseEntity<String> response1 = restTemplate.postForEntity("/auth/signin", input, String.class);
        assertTrue(response.getStatusCode() == HttpStatus.OK &&
                response1.getStatusCode() == HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("sign in test; give SignInRequest(somemail.ru; password01) and sign up this user before with correct mail; must return BAD_REQUEST code")
    void enterTest3() {
        signUpUser("some@mail.ru","password01");
        SignInRequest input = new SignInRequest();
        input.setEmail("some@mail.ru");
        input.setPassword("password01");

        ResponseEntity<String> response = restTemplate.postForEntity("/auth/signin", input, String.class);
        assertSame(HttpStatus.OK, response.getStatusCode());
    }
}