package ma.emsi.ebank.services;

import ma.emsi.ebank.dto.account.AccountDTO;
import ma.emsi.ebank.dto.account.CreateAccountRequest;

import java.util.List;

public interface AccountService {
    AccountDTO createAccount(CreateAccountRequest request);
    List<AccountDTO> getAccountsByClient(Long clientId);
}
