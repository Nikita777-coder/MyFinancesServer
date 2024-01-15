package app.auth.controllers;

import app.auth.dto.request.SignUpRequest;
import app.auth.entities.user.UserEntity;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureTestDatabase(replace = NONE)
class AuthControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("createUser test; give SignUpRequest(some@mail.ru; password01); must return UserDetails(some@mail.ru; )")
    void createTest1() {
        SignUpRequest input = new SignUpRequest("some@mail.ru", "password01");
        ResponseEntity<UserEntity> response = restTemplate.postForEntity("/auth/signup", input, UserEntity.class);
        UserEntity responseUserDetails = response.getBody();
        assertTrue(response.getStatusCode() == HttpStatus.CREATED &&
                Objects.requireNonNull(responseUserDetails).getUsername().equals("some@mail.ru") &&
                Objects.requireNonNull(responseUserDetails).getPassword().equals(""));
    }
}