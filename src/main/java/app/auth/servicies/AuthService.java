package app.auth.servicies;

import app.auth.dto.request.SignInRequest;
import app.auth.dto.request.SignUpRequest;
import app.auth.entities.user.UserEntity;
import app.auth.mappers.UserMapper;
import app.exceptions.PermissionDenyException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserEntity signup(SignUpRequest request) {
        return userService.createUser(userMapper.signUpDtoToUserEntity(request));
    }
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void signin(SignInRequest request) {
        // emailService.sendSomeoneTryToEnterToYourAccountMessage()
        checkActiveAccount(request);
        UserDetails resultUser = userService.getUser(request.getLogin());

        if (!passwordEncoder.matches(request.getPassword(), resultUser.getPassword())) {
            throw new IllegalArgumentException("password is invalid!");
        }
    }
    private void checkActiveAccount(SignInRequest request) {
        if (userService.isActive(request)) {
            // emailService.sendSomeoneTryToEnterToYourAccountMessage+bugSendToDevelopersExpectDecision()
            throw new PermissionDenyException("user is active!");
        }
    }
}
