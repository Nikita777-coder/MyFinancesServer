package app.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UpdateUserDto {
    @NotBlank(message = "email can't be empty!")
    private String requestEmail;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Boolean isActive;
}
