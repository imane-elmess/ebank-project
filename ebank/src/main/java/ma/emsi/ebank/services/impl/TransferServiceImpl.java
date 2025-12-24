package ma.emsi.ebank.services.impl;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.dto.transfer.TransferRequest;
import ma.emsi.ebank.entities.Account;
import ma.emsi.ebank.entities.Client;
import ma.emsi.ebank.entities.Operation;
import ma.emsi.ebank.enums.AccountStatus;
import ma.emsi.ebank.enums.OperationType;
import ma.emsi.ebank.repositories.AccountRepository;
import ma.emsi.ebank.repositories.ClientRepository;
import ma.emsi.ebank.repositories.OperationRepository;
import ma.emsi.ebank.services.TransferService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferServiceImpl implements TransferService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    @Override
    public void transfer(TransferRequest request) {

        Client client = getAuthenticatedClient();

        // 1) compte source : doit appartenir au client
        Account from = accountRepository.findByIdAndOwnerId(request.getFromAccountId(), client.getId())
                .orElseThrow(() -> new IllegalArgumentException("Compte source introuvable ou non autorisé"));

        // 2) compte destination : par RIB
        Account to = accountRepository.findByRib(request.getToRib())
                .orElseThrow(() -> new IllegalArgumentException("Compte destination introuvable"));

        // 3) règles simples de gestion que j'ajoute
        if (from.getId().equals(to.getId()))
            throw new IllegalArgumentException("Impossible de faire un virement vers le même compte.");

        if (from.getStatus() != AccountStatus.OUVERT || to.getStatus() != AccountStatus.OUVERT)
            throw new IllegalArgumentException("Compte bloqué/clôturé : virement impossible.");

        BigDecimal amount = request.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Montant invalide.");

        // solde suffisant
        if (from.getBalance().compareTo(amount) < 0)
            throw new IllegalArgumentException("Solde insuffisant.");

        // 4) mise à jour soldes
        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        Instant now = Instant.now();
        from.setLastMovementAt(now);
        to.setLastMovementAt(now);

        // 5) créer les 2 opérations avec la même référence
        String reference = UUID.randomUUID().toString();

        Operation debit = Operation.builder()
                .account(from)
                .type(OperationType.DEBIT)
                .amount(amount)
                .operationDate(now)
                .description("Virement sortant")
                .counterpartRib(to.getRib())
                .motif(request.getMotif())
                .reference(reference)
                .build();

        Operation credit = Operation.builder()
                .account(to)
                .type(OperationType.CREDIT)
                .amount(amount)
                .operationDate(now)
                .description("Virement entrant")
                .counterpartRib(from.getRib())
                .motif(request.getMotif())
                .reference(reference)
                .build();

        // 6) save (transactionnel)
        accountRepository.save(from);
        accountRepository.save(to);
        operationRepository.save(debit);
        operationRepository.save(credit);
    }

    private Client getAuthenticatedClient() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // chez toi = email
        return clientRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable pour l'utilisateur connecté"));
    }
}
