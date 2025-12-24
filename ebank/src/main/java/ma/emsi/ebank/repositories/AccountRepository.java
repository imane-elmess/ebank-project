package ma.emsi.ebank.repositories;

import ma.emsi.ebank.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByRib(String rib);
    boolean existsByRib(String rib);
    Optional<Account> findByIdAndOwnerId(Long accountId, Long ownerId);
    List<Account> findByOwnerId(Long clientId);
}
