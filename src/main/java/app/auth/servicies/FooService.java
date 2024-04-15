package app.auth.servicies;

import app.auth.dto.foo.FooMarketStock;
import app.auth.dto.foo.FooUserPortfolioRisk;
import app.auth.dto.foo.FooUserStock;
import app.auth.entities.foo.FooMarketStockEntity;
import app.auth.mappers.FooUserStockMapper;
import app.auth.repositories.foo.FooMarketStockRepository;
import app.auth.repositories.foo.FooUserPortfolioRiskRepository;
import app.auth.repositories.foo.FooUserStocksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FooService {
    private final FooUserStockMapper fooUserStockMapper;
    private final FooUserStocksRepository fooUserStocksRepository;
    private final FooMarketStockRepository fooMarketStockRepository;
    private final FooUserPortfolioRiskRepository fooUserPortfolioRiskRepository;
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveUserStocks(List<FooUserStock> userStocks, String email) {
        for (var userStock : userStocks) {
            var newFoo = fooUserStockMapper.fooUserStockToFooUserStockEntity(userStock);
            newFoo.setEmail(email);
            fooUserStocksRepository.save(newFoo);
        }
    }
    public List<FooUserStock> getUserStocks(String email) {
        return fooUserStockMapper.
                fooUserStockEntitiesToFooUserStocks(fooUserStocksRepository.getUserStockEntitiesByEmail(email));
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveMarketStocks(List<FooMarketStock> marketStocks) {
        for (var marketStock : marketStocks) {
            FooMarketStockEntity e = fooUserStockMapper.fooMarketStockToFooMarketStockEntity(marketStock);
            fooMarketStockRepository.save(e);
        }
    }
    public List<FooMarketStock> getMarketStocks() {
        var answer = fooUserStockMapper.
                fooMarketStockEntitiesToFooMarketStocks(fooMarketStockRepository.findAll());;
        return answer;
    }
    public void saveUserPortfolioRisk(FooUserPortfolioRisk userPortfolioRisk) {
        fooUserPortfolioRiskRepository.save(fooUserStockMapper.fooUserPortfolioRiskToFooUserPortfolioRiskEntity(userPortfolioRisk));
    }
    public FooUserPortfolioRisk getUserPortfolioRisk(String email) {
        var ans = fooUserPortfolioRiskRepository.getUserPortfolioRiskEntitiesByEmail(email);

        if (ans.isEmpty()) {
            throw new IllegalArgumentException("there is no risk data in user");
        }

        return fooUserStockMapper.fooUserPortfolioRiskEntityToFooUserPortfolioRisk(ans.get());
    }
}
