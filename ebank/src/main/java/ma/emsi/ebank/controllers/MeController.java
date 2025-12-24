package ma.emsi.ebank.controllers;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.dto.account.AccountDTO;
import ma.emsi.ebank.dto.operation.OperationDTO;
import ma.emsi.ebank.services.MeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
@CrossOrigin(origins = "http://localhost:5173")
public class MeController {

    private final MeService meService;

    // 1) Mes comptes
    @GetMapping("/accounts")
    public ResponseEntity<java.util.List<AccountDTO>> myAccounts() {
        return ResponseEntity.ok(meService.getMyAccounts());
    }

    // 2) Mes opérations (paginées) pour un compte donné
    @GetMapping("/accounts/{accountId}/operations")
    public ResponseEntity<Page<OperationDTO>> myOperations(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(meService.getMyOperations(accountId, page, size));
    }
}
