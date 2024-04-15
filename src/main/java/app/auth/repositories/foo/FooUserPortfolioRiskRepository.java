package app.auth.repositories.foo;

import app.auth.entities.foo.FooUserPortfolioRiskEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FooUserPortfolioRiskRepository extends CrudRepository<FooUserPortfolioRiskEntity, UUID> {
    Optional<FooUserPortfolioRiskEntity> getUserPortfolioRiskEntitiesByEmail(String email);
}
