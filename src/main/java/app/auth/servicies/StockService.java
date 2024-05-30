package app.auth.servicies;

import app.auth.dto.foo.MarketStock;
import app.auth.dto.foo.UserPortfolioRisk;
import app.auth.dto.foo.UserStock;
import app.auth.entities.foo.MarketStockEntity;
import app.auth.mappers.UserStockMapper;
import app.auth.repositories.foo.MarketStockRepository;
import app.auth.repositories.foo.UserPortfolioRiskRepository;
import app.auth.repositories.foo.UserStocksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {
    private final UserStockMapper userStockMapper;
    private final UserStocksRepository userStocksRepository;
    private final MarketStockRepository marketStockRepository;
    private final UserPortfolioRiskRepository userPortfolioRiskRepository;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveUserStocks(List<UserStock> userStocks, String email) {
        for (var userStock : userStocks) {
            var newUserStockEntity = userStockMapper.userStockToUserStockEntity(userStock);
            newUserStockEntity.setEmail(email);
            userStocksRepository.save(newUserStockEntity);
        }
    }

    public List<UserStock> getUserStocks(String email) {
        return userStockMapper.
                userStockEntitiesToUserStocks(userStocksRepository.getUserStockEntitiesByEmail(email));
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveMarketStocks(List<MarketStock> marketStocks) {
        for (var marketStock : marketStocks) {
            MarketStockEntity e = userStockMapper.marketStockToMarketStockEntity(marketStock);
            marketStockRepository.save(e);
        }
    }

    public List<MarketStock> getMarketStocks() {
        var answer = userStockMapper.
                marketStockEntitiesToMarketStocks(marketStockRepository.findAll());
        ;
        return answer;
    }

    public void saveUserPortfolioRisk(UserPortfolioRisk userPortfolioRisk) {
        userPortfolioRiskRepository.save(userStockMapper.userPortfolioRiskToUserPortfolioRiskEntity(userPortfolioRisk));
    }

    public UserPortfolioRisk getUserPortfolioRisk(String email) {
        var ans = userPortfolioRiskRepository.getUserPortfolioRiskEntitiesByEmail(email);

        if (ans.isEmpty()) {
            throw new IllegalArgumentException("there is no risk data in user");
        }

        return userStockMapper.userPortfolioRiskEntityToUserPortfolioRisk(ans.get());
    }
}
