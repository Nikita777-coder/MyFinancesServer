package app.auth.repositories;

import app.auth.entities.FooUserStockEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FooUserStocksRepository extends CrudRepository<FooUserStockEntity, UUID> {
    List<FooUserStockEntity> getUserStockEntitiesByEmail(String email);
}
