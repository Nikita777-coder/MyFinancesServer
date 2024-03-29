package app.auth.mappers;

import app.auth.dto.UserOutData;
import app.auth.dto.request.SignUpRequest;
import app.auth.dto.request.UpdateUserDto;
import app.auth.entities.user.UserEntity;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(ignore = true, target = "id")
    UserEntity signUpDtoToUserEntity(SignUpRequest request);
    UserOutData userEntityToUserOutData(UserEntity userEntity);
    @Mapping(target = "email", source = "requestEmail")
    UserEntity updateUserDtoToUserEntity(UpdateUserDto updateUserDto);
    @Mapping(target = "requestEmail", source = "email")
    UpdateUserDto userEntityToUpdateUserDto(UserEntity userEntity);
    @BeforeMapping
    default void encodePassword(SignUpRequest request) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        request.setPassword(encoder.encode(request.getPassword()));
    }
}