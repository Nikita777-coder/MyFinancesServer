package app.auth.mappers;

import app.auth.dto.UserOutData;
import app.auth.dto.request.SignUpRequest;
import app.auth.dto.request.UpdateUserDto;
import app.auth.entities.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(ignore = true, target = "id")
    UserEntity signUpDtoToUserEntity(SignUpRequest request, PasswordEncoder encoder);
    UserOutData userEntityToUserOutData(UserEntity userEntity);
    @Mapping(target = "email", source = "updateUserDto.requestEmail")
    UserEntity updateUserDtoToUserEntity(UpdateUserDto updateUserDto, PasswordEncoder encoder);
    @Mapping(target = "requestEmail", source = "email")
    UpdateUserDto userEntityToUpdateUserDto(UserEntity userEntity);
    @BeforeMapping
    default void encodePassword(SignUpRequest request, PasswordEncoder encoder) {
        // request.setPassword(encoder.encode(request.getPassword()));
    }
    @BeforeMapping
    default void encodePassword(UpdateUserDto request, PasswordEncoder encoder) {
        // request.setPassword(encoder.encode(request.getPassword()));
    }
    @AfterMapping
    default void setActiveToSignUp(SignUpRequest request, @MappingTarget UserEntity target) {
        target.setIsActive(false);
    }
}