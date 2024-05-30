package app.auth.mappers;

import app.auth.dto.foo.MarketStock;
import app.auth.dto.foo.UserPortfolioRisk;
import app.auth.dto.foo.UserStock;
import app.auth.entities.foo.MarketStockEntity;
import app.auth.entities.foo.UserPortfolioRiskEntity;
import app.auth.entities.foo.UserStockEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserStockMapper {
    UserStockEntity userStockToUserStockEntity(UserStock userStock);

    List<UserStock> userStockEntitiesToUserStocks(List<UserStockEntity> userStockEntities);

    MarketStockEntity marketStockToMarketStockEntity(MarketStock marketStock);

    List<MarketStock> marketStockEntitiesToMarketStocks(List<MarketStockEntity> marketStockEntities);

    UserPortfolioRiskEntity userPortfolioRiskToUserPortfolioRiskEntity(UserPortfolioRisk userPortfolioRisk);

    UserPortfolioRisk userPortfolioRiskEntityToUserPortfolioRisk(UserPortfolioRiskEntity userPortfolioRiskEntity);
}
