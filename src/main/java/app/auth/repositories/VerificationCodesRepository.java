package app.auth.repositories;

import app.auth.entities.VerificationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationCodesRepository extends JpaRepository<VerificationCodeEntity, UUID> {
    Optional<VerificationCodeEntity> findByEmail(String email);
    @Transactional(timeout = 7200, isolation = Isolation.SERIALIZABLE)
    @Modifying
    @Query("delete FROM VerificationCodeEntity vce where vce.expiredAt <= current_timestamp")
    void deleteAllInvalidVerificationCodes();
}
