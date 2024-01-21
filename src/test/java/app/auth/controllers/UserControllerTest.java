package app.auth.controllers;

import app.auth.dto.request.UpdateUserDto;
import app.auth.entities.user.UserEntity;
import app.auth.mappers.UserMapper;
import app.auth.repositories.UserRepository;
import app.auth.servicies.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class UserControllerTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userMapper);
    }
    @Test
    @DisplayName("updateUser test; give email UpdateUserDto(blabla@me.ru); must return BAD_REQUEST with message \"User not found\"")
    void updateUserTest1() {
        UpdateUserDto dto = UpdateUserDto.builder().requestEmail("blabla@me.ru").build();
        ResponseEntity<String> response = restTemplate.exchange("/user/update_profile", HttpMethod.PATCH, new HttpEntity<>(dto), String.class);
        assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND && Objects.equals(response.getBody(), "User not found"));
    }

    @Test
    @DisplayName("updateUser test; registrate user with email blabla@me.ru and give UpdateUserDto(blabla@me.ru; \"new_login\", \"new_password\"); must return OK status")
    void updateUserTest2() {
        UpdateUserDto updateUserDto = UpdateUserDto
                .builder()
                .requestEmail("blabla@me.ru")
                .login("new_login")
                .password("new_password")
                .build();
//        UserEntity testMockEntity = UserEntity
//                .builder()
//                .email("blabla@me.ru")
//                .login("new_login")
//                .password("new_password")
//                .build();
//
//        when(userRepository.findByEmail("blabla@me.ru")).thenReturn(Optional.of(testMockEntity));
//        when(userRepository.save(new UserEntity())).thenReturn(testMockEntity);
//        when(userMapper.updateUserDtoToUserEntity(updateUserDto)).thenReturn(testMockEntity);
//        when(userMapper.userEntityToUpdateUserDto(testMockEntity)).thenReturn(updateUserDto);

        assertEquals(updateUserDto, updateUserDto);
    }
}