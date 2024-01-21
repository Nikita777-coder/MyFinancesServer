package app.auth.controllers;

import app.auth.dto.request.UpdateUserDto;
import app.auth.servicies.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/is_email_exist")
    public ResponseEntity<Boolean> getEmail(@RequestBody String email) {
        Boolean response = userService.isEmailExist(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/update_profile")
    public ResponseEntity<UpdateUserDto> updateUser(@Valid @RequestBody UpdateUserDto userUpdatedData) {
        UpdateUserDto response = userService.updateUser(userUpdatedData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
