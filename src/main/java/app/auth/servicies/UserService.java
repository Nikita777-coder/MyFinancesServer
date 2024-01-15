package app.auth.servicies;

import app.auth.dto.request.SignInRequest;
import app.auth.entities.user.UserEntity;
import app.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
//    public UserDetails getCurrentUser() {
//        UserDetails userDetails = extractCurrentUser();
//
//        return getUser(userDetails.getUsername());
//    }
    public UserEntity createUser(UserEntity entity) {
        if (userRepository.findByLogin(entity.getLogin()).isPresent()) {
            throw new IllegalArgumentException("user with this login exists");
        }
        
        return userRepository.save(entity);
    }
    public UserDetails getUser(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public boolean isActive(SignInRequest request) {
        Optional<UserEntity> foundUser;

        if (request.getLogin() == null) {
            foundUser = userRepository.findByEmail(request.getEmail());
        } else {
            foundUser = userRepository.findByLogin(request.getLogin());
        }

        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return foundUser.get().isEnabled();
    }
    public boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
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
