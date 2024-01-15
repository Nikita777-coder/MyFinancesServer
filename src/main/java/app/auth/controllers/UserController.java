package app.auth.controllers;

import app.auth.servicies.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
