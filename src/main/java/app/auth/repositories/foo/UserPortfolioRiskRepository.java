package app.auth.repositories.foo;

import app.auth.entities.foo.UserPortfolioRiskEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPortfolioRiskRepository extends CrudRepository<UserPortfolioRiskEntity, UUID> {
    Optional<UserPortfolioRiskEntity> getUserPortfolioRiskEntitiesByEmail(String email);
}
