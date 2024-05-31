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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(isolation = Isolation.READ_COMMITTED)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private UserService self;

    @Override
    @CachePut(value = "users", key = "#result.email")
    public UserOutData createUser(UserEntity entity) {
        if (userRepository.findByEmail(entity.getEmail()).isPresent()) {
            log.warn("User with email {} already exists", entity.getEmail());
            throw new IllegalArgumentException("user with this email exists");
        }

        log.info("Creating user with email {}", entity.getEmail());
        var ent = userRepository.save(entity);
        return userMapper.userEntityToUserOutData(ent);
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public UserEntity getUserByEmail(String email) {
        log.debug("Fetching user by email {}", email);
        Optional<UserEntity> foundUser = userRepository.findByEmail(email);

        if (foundUser.isEmpty()) {
            log.error("User not found by email {}", email);
            throw new UsernameNotFoundException("User not found");
        }

        return foundUser.get();
    }

    @Override
    public UserEntity getUser(SignInRequest request) {
        if (request.getLogin() == null) {
            return self.getUserByEmail(request.getEmail());
        }

        return getUserByLogin(request.getLogin());
    }

    @Override
    public boolean isActive(SignInRequest request) {
        return getUser(request).getIsActive();
    }

    @Override
    public boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @CacheEvict(value = "users", key = "#userUpdatedData.requestEmail")
    public UpdateUserDto updateUser(UpdateUserDto userUpdatedData) {
        log.debug("Updating user with email {}", userUpdatedData.getRequestEmail());
        UserEntity foundUser = self.getUserByEmail(userUpdatedData.getRequestEmail());

        UserEntity updatedUser = userMapper.updateUserDtoToUserEntity(userUpdatedData, passwordEncoder);
        updatedUser.setId(foundUser.getId());
        updatedUser.setIsActive(false);

        return updateUser(updatedUser);
    }

    @Override
    @CacheEvict(value = "users", key = "#changePasswordDto.email")
    public UpdateUserDto updateUser(ChangePasswordDto changePasswordDto) {
        log.debug("Updating password for user with email {}", changePasswordDto.getEmail());
        UserEntity foundUser = self.getUserByEmail(changePasswordDto.getEmail());
        foundUser.setPassword(changePasswordDto.getPassword());
        foundUser.setIsActive(false);

        return updateUser(foundUser);
    }

    @Override
    public UpdateUserDto updateUser(UserEntity updatedUserEntity) {
        return userMapper.userEntityToUpdateUserDto(userRepository.save(updatedUserEntity));
    }

    private UserEntity getUserByLogin(String login) {
        log.debug("Fetching user by login {}", login);
        Optional<UserEntity> foundUser = userRepository.findByLogin(login);

        if (foundUser.isEmpty()) {
            log.error("User not found by login {}", login);
            throw new UsernameNotFoundException("User not found");
        }

        return foundUser.get();
    }

    @Override
    @CacheEvict(value = "users", key = "#email")
    public void deleteUserByEmail(String email) {
        log.info("Deleting user with email {}", email);
        userRepository.deleteByEmail(email);
    }
}
