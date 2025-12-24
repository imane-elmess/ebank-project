package ma.emsi.ebank.services.impl;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.entities.User;
import ma.emsi.ebank.repositories.UserRepository;
import ma.emsi.ebank.services.ChangePasswordService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChangePasswordServiceImpl implements ChangePasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(String oldPassword, String newPassword) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //= email

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        // Vérifier ancien mot de passe
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }

        // Enregistrer nouveau
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
