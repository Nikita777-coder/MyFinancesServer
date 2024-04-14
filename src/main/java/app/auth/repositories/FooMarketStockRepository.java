package app.auth.repositories;

import app.auth.dto.FooMarketStock;
import app.auth.entities.FooMarketStockEntity;
import app.auth.entities.FooUserStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FooMarketStockRepository extends JpaRepository<FooMarketStockEntity, UUID> {
}
