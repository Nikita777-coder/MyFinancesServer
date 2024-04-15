package app.auth.entities.foo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "foo_market_stocks")
@Getter
@Setter
@NoArgsConstructor
public class FooMarketStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String symbol;
    private String companyName;
    private double latestPrice;
    private int count;
    private double totalCost;
    private double growMoney;
    private double growPercentage;
}
