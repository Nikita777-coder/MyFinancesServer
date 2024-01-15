package app.auth.servicies;

import app.auth.dto.UserOutData;
import app.auth.dto.request.SignInRequest;
import app.auth.dto.request.UpdateUserDto;
import app.auth.entities.user.UserEntity;
import app.auth.mappers.UserMapper;
import app.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
//    public UserDetails getCurrentUser() {
//        UserDetails userDetails = extractCurrentUser();
//
//        return getUser(userDetails.getUsername());
//    }
    public UserOutData createUser(UserEntity entity) {
        if (userRepository.findByEmail(entity.getEmail()).isPresent()) {
            throw new IllegalArgumentException("user with this login exists");
        }

        entity.setIsActive(true);
        return userMapper.userEntityToUserOutData(userRepository.save(entity));
    }
    public UserEntity getUser(SignInRequest request) {
        Optional<UserEntity> foundUser;

        if (request.getLogin() == null) {
            foundUser = userRepository.findByEmail(request.getEmail());
        } else {
            foundUser = userRepository.findByLogin(request.getLogin());
        }

        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return foundUser.get();
    }
    public boolean isActive(SignInRequest request) {
        return getUser(request).getIsActive();
    }
    public boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
    public UpdateUserDto updateUser(UpdateUserDto userUpdatedData) {
        SignInRequest request = new SignInRequest();
        request.setEmail(userUpdatedData.getRequestEmail());
        getUser(request);

        return userMapper.userEntityToUpdateUserDto(userRepository.save(userMapper.updateUserDtoToUserEntity(userUpdatedData)));
    }
//    private UserDetails extractCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return null;
//        }
//
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof UserDetails userDetails) {
//            return userDetails;
//        }
//
//        throw new IllegalStateException("Unexpected authentication principal: " + principal);
//    }
}
