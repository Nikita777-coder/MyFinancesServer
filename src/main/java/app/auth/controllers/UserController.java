package app.auth.controllers;

import app.auth.dto.UserOutData;
import app.auth.dto.request.ChangePasswordDto;
import app.auth.dto.request.UpdateUserDto;
import app.auth.mappers.UserMapper;
import app.auth.servicies.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/get_user_by_email")
    public ResponseEntity<UserOutData> getUserByEmail(@RequestParam String email) {
        UserOutData response = userMapper.userEntityToUserOutData(userService.getUserByEmail(email));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/update_profile")
    public ResponseEntity<UpdateUserDto> updateUser(@Valid @RequestBody UpdateUserDto userUpdatedData) {
        UpdateUserDto response = userService.updateUser(userUpdatedData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/update_password_profile")
    public ResponseEntity<UserOutData> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        var res = userService.updateUser(changePasswordDto);
        return new ResponseEntity<>(userMapper.updateUserDtoToUserOutData(res), HttpStatus.OK);
    }
}
