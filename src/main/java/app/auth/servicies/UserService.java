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
            throw new IllegalArgumentException("user with this email exists");
        }

        //entity.setIsActive(true);
        return userMapper.userEntityToUserOutData(userRepository.save(entity));
    }
    public UserEntity getUser(SignInRequest request) {
        if (request.getLogin() == null) {
            return getUserByEmail(request.getEmail());
        }

        return getUserByLogin(request.getLogin());
    }
    public boolean isActive(SignInRequest request) {
        return getUser(request).getIsActive();
    }
    public boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    public UpdateUserDto updateUser(UpdateUserDto userUpdatedData) {
        UserEntity foundUser = getUserByEmail(userUpdatedData.getRequestEmail());

        UserEntity updatedUser = userMapper.updateUserDtoToUserEntity(userUpdatedData);
        updatedUser.setId(foundUser.getId());

        return updateUser(updatedUser);
    }

    public UpdateUserDto updateUser(UserEntity updatedUserEntity) {
        return userMapper.userEntityToUpdateUserDto(userRepository.save(updatedUserEntity));
    }

    private UserEntity getUserByEmail(String email) {
        Optional<UserEntity> foundUser = userRepository.findByEmail(email);

        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return foundUser.get();
    }

    private UserEntity getUserByLogin(String login) {
        Optional<UserEntity> foundUser = userRepository.findByLogin(login);

        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return foundUser.get();
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
