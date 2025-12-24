package ma.emsi.ebank.services;

import ma.emsi.ebank.dto.client.ClientDTO;
import ma.emsi.ebank.dto.client.CreateClientRequest;

import java.util.List;

public interface ClientService {

    ClientDTO createClient(CreateClientRequest request);

    ClientDTO getClientById(Long id);

    List<ClientDTO> getAllClients();
}
