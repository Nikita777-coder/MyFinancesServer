package app.auth.dto.jwtresponse;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class JwtResponse {
    private String token;
}
