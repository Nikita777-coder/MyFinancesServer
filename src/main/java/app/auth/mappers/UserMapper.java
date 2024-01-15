package app.auth.mappers;

import app.auth.dto.request.SignUpRequest;
import app.auth.entities.user.UserEntity;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "role")
    @Mapping(ignore = true, target = "authorities")
    UserEntity signUpDtoToUserEntity(SignUpRequest request);
    @BeforeMapping
    default void encodePassword(SignUpRequest request) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        request.setPassword(encoder.encode(request.getPassword()));
    }
}