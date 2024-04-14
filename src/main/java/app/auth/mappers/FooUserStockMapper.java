package app.auth.mappers;

import app.auth.dto.FooMarketStock;
import app.auth.dto.FooUserStock;
import app.auth.entities.FooMarketStockEntity;
import app.auth.entities.FooUserStockEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FooUserStockMapper {
    FooUserStockEntity fooUserStockToFooUserStockEntity(FooUserStock fooUserStock);
    List<FooUserStock> fooUserStockEntitiesToFooUserStocks(List<FooUserStockEntity> fooUserStockEntity);
    FooMarketStockEntity fooMarketStockToFooMarketStockEntity(FooMarketStock fooMarketStock);
    List<FooMarketStock> fooMarketStockEntitiesToFooMarketStocks(List<FooMarketStockEntity> fooMarketStocks);
}
