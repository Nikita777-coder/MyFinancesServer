package app.auth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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
public class FooUserStockEntity {
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
