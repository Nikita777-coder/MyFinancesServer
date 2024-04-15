package app.auth.entities.foo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user_portfolio_risks",
    indexes = {@Index(name = "email_index", columnList = "email", unique = true)})
@Getter
@Setter
@NoArgsConstructor
public class FooUserPortfolioRiskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;
    private Double currentRisk;
    private Double previousRisk;
}
