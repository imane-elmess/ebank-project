package ma.emsi.ebank.services.impl;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.dto.account.AccountDTO;
import ma.emsi.ebank.dto.operation.OperationDTO;
import ma.emsi.ebank.entities.Account;
import ma.emsi.ebank.entities.Client;
import ma.emsi.ebank.entities.Operation;
import ma.emsi.ebank.repositories.AccountRepository;
import ma.emsi.ebank.repositories.ClientRepository;
import ma.emsi.ebank.repositories.OperationRepository;
import ma.emsi.ebank.services.MeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MeServiceImpl implements MeService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    @Override
    public List<AccountDTO> getMyAccounts() {
        Client client = getAuthenticatedClient();

        // Tri “dernier compte utilisé” en haut (lastMovementAt desc, null en bas)
        return accountRepository.findByOwnerId(client.getId()).stream()
                .sorted(Comparator.comparing(Account::getLastMovementAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::mapAccount)
                .toList();
    }

    @Override
    public Page<OperationDTO> getMyOperations(Long accountId, int page, int size) {
        Client client = getAuthenticatedClient();

        Account account = accountRepository.findByIdAndOwnerId(accountId, client.getId())
                .orElseThrow(() -> new IllegalArgumentException("Compte introuvable ou non autorisé"));

        Page<Operation> ops = operationRepository.findByAccountOrderByOperationDateDesc(
                account, PageRequest.of(page, size)
        );

        return ops.map(this::mapOperation);
    }

    private Client getAuthenticatedClient() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //= email

        return clientRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable pour l'utilisateur connecté"));
    }

    private AccountDTO mapAccount(Account a) {
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

    private OperationDTO mapOperation(Operation o) {
        return OperationDTO.builder()
                .id(o.getId())
                .description(o.getDescription())
                .type(o.getType())
                .amount(o.getAmount())
                .operationDate(o.getOperationDate())
                .counterpartRib(o.getCounterpartRib())
                .motif(o.getMotif())
                .reference(o.getReference())
                .build();
    }
}
