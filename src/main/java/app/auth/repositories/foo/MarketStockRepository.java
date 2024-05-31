package app.auth.repositories.foo;

import app.auth.entities.foo.MarketStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MarketStockRepository extends JpaRepository<MarketStockEntity, UUID> {
}
