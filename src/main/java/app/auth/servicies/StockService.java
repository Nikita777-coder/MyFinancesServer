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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    private final UserStockMapper userStockMapper;
    private final UserStocksRepository userStocksRepository;
    private final MarketStockRepository marketStockRepository;
    private final UserPortfolioRiskRepository userPortfolioRiskRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveUserStocks(List<UserStock> userStocks, String email) {
        log.info("Saving stocks for user with email {}", email);
        for (var userStock : userStocks) {
            var newUserStockEntity = userStockMapper.userStockToUserStockEntity(userStock);
            newUserStockEntity.setEmail(email);
            userStocksRepository.save(newUserStockEntity);
        }
    }

    public List<UserStock> getUserStocks(String email) {
        log.debug("Fetching stocks for user with email {}", email);
        return userStockMapper.userStockEntitiesToUserStocks(userStocksRepository.getUserStockEntitiesByEmail(email));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveMarketStocks(List<MarketStock> marketStocks) {
        log.info("Saving market stocks");
        for (var marketStock : marketStocks) {
            MarketStockEntity e = userStockMapper.marketStockToMarketStockEntity(marketStock);
            marketStockRepository.save(e);
        }
    }

    public List<MarketStock> getMarketStocks() {
        log.debug("Fetching market stocks");
        return userStockMapper.marketStockEntitiesToMarketStocks(marketStockRepository.findAll());
    }

    public void saveUserPortfolioRisk(UserPortfolioRisk userPortfolioRisk) {
        log.info("Saving portfolio risk for user with email {}", userPortfolioRisk.getEmail());
        userPortfolioRiskRepository.save(userStockMapper.userPortfolioRiskToUserPortfolioRiskEntity(userPortfolioRisk));
    }

    public UserPortfolioRisk getUserPortfolioRisk(String email) {
        log.debug("Fetching portfolio risk for user with email {}", email);
        var ans = userPortfolioRiskRepository.getUserPortfolioRiskEntitiesByEmail(email);

        if (ans.isEmpty()) {
            log.error("No risk data found for user with email {}", email);
            throw new IllegalArgumentException("there is no risk data in user");
        }

        return userStockMapper.userPortfolioRiskEntityToUserPortfolioRisk(ans.get());
    }
}
