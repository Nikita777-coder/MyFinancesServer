package app.auth.servicies;

import app.auth.dto.UserOutData;
import app.auth.dto.request.ChangePasswordDto;
import app.auth.dto.request.SignInRequest;
import app.auth.dto.request.UpdateUserDto;
import app.auth.entities.user.UserEntity;

public interface UserService {
    UserOutData createUser(UserEntity entity);

    UserEntity getUserByEmail(String email);

    UserEntity getUser(SignInRequest request);

    boolean isActive(SignInRequest request);

    boolean isEmailExist(String email);

    UpdateUserDto updateUser(UpdateUserDto userUpdatedData);

    UpdateUserDto updateUser(ChangePasswordDto changePasswordDto);

    UpdateUserDto updateUser(UserEntity updatedUserEntity);

    void deleteUserByEmail(String email);
}
