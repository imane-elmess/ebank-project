package ma.emsi.ebank.config;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.entities.User;
import ma.emsi.ebank.enums.UserRole;
import ma.emsi.ebank.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 1) Vérifier si l'agent existe déjà
        String username = "agent@ebank.com";

        if (userRepository.existsByUsername(username)) {
            System.out.println("Utilisateur agent déjà présent : " + username);
            return; // on ne recrée pas
        }

        // 2) Définir un mot de passe brut (à communiquer au début)
        String rawPassword = "agent123";

        // 3) Encoder le mot de passe
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 4) Construire un objet User avec rôle AGENT_GUICHET
        User agent = User.builder()
                .username(username)
                .email(username)
                .password(encodedPassword)
                .role(UserRole.AGENT_GUICHET)
                .enabled(true)
                .build();

        // 5) Sauvegarder en base
        userRepository.save(agent);

        System.out.println("Utilisateur AGENT_GUICHET créé : " + username +
                " | mot de passe = " + rawPassword);
    }
}
