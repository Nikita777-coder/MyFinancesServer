package app.auth.servicies;

import app.auth.dto.foo.MarketStock;
import app.auth.dto.foo.UserPortfolioRisk;
import app.auth.dto.foo.UserStock;

import java.util.List;

public interface StockService {

    public void saveUserStocks(List<UserStock> userStocks, String email);

    public List<UserStock> getUserStocks(String email);

    public void saveMarketStocks(List<MarketStock> marketStocks);

    public List<MarketStock> getMarketStocks();

    public void saveUserPortfolioRisk(UserPortfolioRisk userPortfolioRisk);

    public UserPortfolioRisk getUserPortfolioRisk(String email);
}
