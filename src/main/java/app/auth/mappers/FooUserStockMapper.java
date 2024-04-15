package app.auth.mappers;

import app.auth.dto.foo.FooMarketStock;
import app.auth.dto.foo.FooUserPortfolioRisk;
import app.auth.dto.foo.FooUserStock;
import app.auth.entities.foo.FooMarketStockEntity;
import app.auth.entities.foo.FooUserPortfolioRiskEntity;
import app.auth.entities.foo.FooUserStockEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FooUserStockMapper {
    FooUserStockEntity fooUserStockToFooUserStockEntity(FooUserStock fooUserStock);
    List<FooUserStock> fooUserStockEntitiesToFooUserStocks(List<FooUserStockEntity> fooUserStockEntity);
    FooMarketStockEntity fooMarketStockToFooMarketStockEntity(FooMarketStock fooMarketStock);
    List<FooMarketStock> fooMarketStockEntitiesToFooMarketStocks(List<FooMarketStockEntity> fooMarketStocks);
    FooUserPortfolioRiskEntity fooUserPortfolioRiskToFooUserPortfolioRiskEntity(FooUserPortfolioRisk fooUserPortfolioRisk);
    FooUserPortfolioRisk fooUserPortfolioRiskEntityToFooUserPortfolioRisk(FooUserPortfolioRiskEntity fooUserPortfolioRiskEntity);
}
