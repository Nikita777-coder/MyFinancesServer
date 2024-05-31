package app.auth.entities.foo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "foo_user_stocks",
    indexes = {@Index(name = "email_index", columnList = "email")}
)
@Getter
@Setter
@NoArgsConstructor
public class UserStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;
    private String companyName;
    private int count;
    private BigDecimal cost;
    private BigDecimal invested;
    private BigDecimal income;
    private double diffOfOneStockCost;
    private double fraction;
}
