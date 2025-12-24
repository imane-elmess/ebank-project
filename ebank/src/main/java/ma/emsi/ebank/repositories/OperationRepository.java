package ma.emsi.ebank.repositories;

import ma.emsi.ebank.entities.Account;
import ma.emsi.ebank.entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    Page<Operation> findByAccountOrderByOperationDateDesc(Account account, Pageable pageable);
}
