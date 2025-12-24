package ma.emsi.ebank.repositories;

import ma.emsi.ebank.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByIdentityNumber(String identityNumber);

    Optional<Client> findByEmail(String email);

    boolean existsByIdentityNumber(String identityNumber);

    boolean existsByEmail(String email);
}
