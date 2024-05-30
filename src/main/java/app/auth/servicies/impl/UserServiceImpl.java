package app.auth.servicies.impl;

import app.auth.dto.UserOutData;
import app.auth.dto.request.ChangePasswordDto;
import app.auth.dto.request.SignInRequest;
import app.auth.dto.request.UpdateUserDto;
import app.auth.entities.user.UserEntity;
import app.auth.mappers.UserMapper;
import app.auth.repositories.UserRepository;
import app.auth.servicies.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private UserService self;

    @CachePut(value = "users", key = "#result.email")
    public UserOutData createUser(UserEntity entity) {
        if (userRepository.findByEmail(entity.getEmail()).isPresent()) {
            throw new IllegalArgumentException("user with this email exists");
        }

        var ent = userRepository.save(entity);
        return userMapper.userEntityToUserOutData(ent);
    }

    @Cacheable(value = "users", key = "#email")
    public UserEntity getUserByEmail(String email) {
        Optional<UserEntity> foundUser = userRepository.findByEmail(email);

        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return foundUser.get();
    }

    public UserEntity getUser(SignInRequest request) {
        if (request.getLogin() == null) {
            return self.getUserByEmail(request.getEmail());
        }

        return getUserByLogin(request.getLogin());
    }

    public boolean isActive(SignInRequest request) {
        return getUser(request).getIsActive();
    }

    public boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @CacheEvict(value = "users", key = "#userUpdatedData.requestEmail")
    public UpdateUserDto updateUser(UpdateUserDto userUpdatedData) {
        UserEntity foundUser = self.getUserByEmail(userUpdatedData.getRequestEmail());

        UserEntity updatedUser = userMapper.updateUserDtoToUserEntity(userUpdatedData, passwordEncoder);
        updatedUser.setId(foundUser.getId());
        updatedUser.setIsActive(false);

        return updateUser(updatedUser);
    }

    @CacheEvict(value = "users", key = "#changePasswordDto.email")
    public UpdateUserDto updateUser(ChangePasswordDto changePasswordDto) {
        UserEntity foundUser = self.getUserByEmail(changePasswordDto.getEmail());
        foundUser.setPassword(changePasswordDto.getPassword());
        foundUser.setIsActive(false);

        return updateUser(foundUser);
    }

    public UpdateUserDto updateUser(UserEntity updatedUserEntity) {
        return userMapper.userEntityToUpdateUserDto(userRepository.save(updatedUserEntity));
    }

    private UserEntity getUserByLogin(String login) {
        Optional<UserEntity> foundUser = userRepository.findByLogin(login);

        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return foundUser.get();
    }

    @CacheEvict(value = "users", key = "#email")
    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }
}
