package ma.emsi.ebank.services.impl;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.dto.account.AccountDTO;
import ma.emsi.ebank.dto.account.CreateAccountRequest;
import ma.emsi.ebank.entities.Account;
import ma.emsi.ebank.entities.Client;
import ma.emsi.ebank.enums.AccountStatus;
import ma.emsi.ebank.repositories.AccountRepository;
import ma.emsi.ebank.repositories.ClientRepository;
import ma.emsi.ebank.services.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    @Override
    public AccountDTO createAccount(CreateAccountRequest request) {

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable avec id=" + request.getClientId()));

        BigDecimal initial = request.getInitialBalance() == null ? BigDecimal.ZERO : request.getInitialBalance();

        Account account = Account.builder()
                .rib(generateUniqueRib24())
                .balance(initial)
                .status(AccountStatus.OUVERT)
                .createdAt(Instant.now())
                .owner(client)
                .build();

        Account saved = accountRepository.save(account);

        return mapToDTO(saved);
    }

    @Override
    public List<AccountDTO> getAccountsByClient(Long clientId) {
        return accountRepository.findByOwnerId(clientId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private AccountDTO mapToDTO(Account a) {
        return AccountDTO.builder()
                .id(a.getId())
                .rib(a.getRib())
                .balance(a.getBalance())
                .status(a.getStatus())
                .createdAt(a.getCreatedAt())
                .lastMovementAt(a.getLastMovementAt())
                .clientId(a.getOwner() != null ? a.getOwner().getId() : null)
                .build();
    }

    // Générer un RIB de 24 chiffres
    private String generateUniqueRib24() {
        String rib;
        do {
            rib = randomDigits(24);
        } while (accountRepository.existsByRib(rib));
        return rib;
    }

    private String randomDigits(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return sb.toString();
    }
}
