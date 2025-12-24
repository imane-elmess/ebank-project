package ma.emsi.ebank.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.dto.transfer.TransferRequest;
import ma.emsi.ebank.services.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "http://localhost:5173")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        transferService.transfer(request);
        return ResponseEntity.noContent().build(); // 204
    }
}
