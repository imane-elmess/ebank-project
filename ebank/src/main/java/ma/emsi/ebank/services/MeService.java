package ma.emsi.ebank.services;

import ma.emsi.ebank.dto.account.AccountDTO;
import ma.emsi.ebank.dto.operation.OperationDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MeService {
    List<AccountDTO> getMyAccounts();
    Page<OperationDTO> getMyOperations(Long accountId, int page, int size);
}
