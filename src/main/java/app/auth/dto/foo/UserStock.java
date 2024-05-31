package app.auth.dto.foo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserStock {
    private String symbol;
    private String companyName;
    private int count;
    private BigDecimal cost;
    private BigDecimal invested;
    private BigDecimal income;
    private double currentCostOfStock;
    private double diffOfOneStockCost;
    private double fraction;
}
