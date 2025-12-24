package ma.emsi.ebank.services.impl;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.dto.client.ClientDTO;
import ma.emsi.ebank.dto.client.CreateClientRequest;
import ma.emsi.ebank.entities.Client;
import ma.emsi.ebank.entities.User;
import ma.emsi.ebank.enums.UserRole;
import ma.emsi.ebank.repositories.ClientRepository;
import ma.emsi.ebank.repositories.UserRepository;
import ma.emsi.ebank.services.ClientService;
import ma.emsi.ebank.services.MailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    public ClientDTO createClient(CreateClientRequest request) {

        // 1)  unicité n° identité
        if (clientRepository.existsByIdentityNumber(request.getIdentityNumber())) {
            throw new IllegalArgumentException("Un client avec ce numéro d'identité existe déjà.");
        }

        // 2) je vérifie unicité email côté client
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà.");
        }

        // 3) je vérifie unicité email côté user (login)
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
        }

        // 4) je crée le User applicatif (login / mot de passe)
        //TODO: plus tard on va encoder le mot de passe avec PasswordEncoder + JWT
        String rawPassword = java.util.UUID.randomUUID().toString().substring(0, 10);
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = User.builder()
                .username(request.getEmail())
                .email(request.getEmail())
                .password(encodedPassword)   // 3️- On stocke la version encodée
                .role(UserRole.CLIENT)
                .enabled(true)
                .createdAt(Instant.now())
                .build();


        User savedUser = userRepository.save(user);

        // 5) Créer le Client
        Client client = Client.builder()
                .identityNumber(request.getIdentityNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .postalAddress(request.getPostalAddress())
                .createdAt(Instant.now())
                .user(savedUser)
                .build();

        Client savedClient = clientRepository.save(client);
        System.out.println("CLIENT CREATED => email=" + request.getEmail() + " password=" + rawPassword);

        mailService.sendCredentials(request.getEmail(), request.getEmail(), rawPassword);

        // 6) Retourner un DTO
        return mapToDTO(savedClient);
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable avec id = " + id));

        return mapToDTO(client);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ====== MAPPER ======
    private ClientDTO mapToDTO(Client client) {
        return ClientDTO.builder()
                .id(client.getId())
                .identityNumber(client.getIdentityNumber())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .birthDate(client.getBirthDate())
                .email(client.getEmail())
                .postalAddress(client.getPostalAddress())
                .createdAt(client.getCreatedAt())
                .build();
    }
}
