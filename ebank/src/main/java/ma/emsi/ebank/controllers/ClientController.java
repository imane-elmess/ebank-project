package ma.emsi.ebank.controllers;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.dto.client.ClientDTO;
import ma.emsi.ebank.dto.client.CreateClientRequest;
import ma.emsi.ebank.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:5173")
public class ClientController {

    private final ClientService clientService;

    //créer client
    @PostMapping
    @PreAuthorize("hasRole('AGENT_GUICHET')")
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody CreateClientRequest request) {
        ClientDTO client = clientService.createClient(request);
        return ResponseEntity.ok(client);
    }

    //getallclients
    @GetMapping
    @PreAuthorize("hasRole('AGENT_GUICHET')")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }
    //par id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('AGENT_GUICHET')")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

}
