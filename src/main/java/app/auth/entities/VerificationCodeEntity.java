package app.auth.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verification_codes")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class VerificationCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    @Column(name = "code")
    private String verificationCode;

    private LocalDateTime expiredAt;
}
