package app.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FooMarketStock {
    private String symbol;
    private String companyName;
    private double latestPrice;
    private int count;
    private double totalCost;
    private double growMoney;
    private double growPercentage;
}
