package ma.emsi.ebank.services.impl;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.entities.PasswordResetToken;
import ma.emsi.ebank.entities.User;
import ma.emsi.ebank.repositories.PasswordResetTokenRepository;
import ma.emsi.ebank.repositories.UserRepository;
import ma.emsi.ebank.services.MailService;
import ma.emsi.ebank.services.PasswordResetService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    // 15 minutes (tu peux changer)
    private static final Duration TOKEN_TTL = Duration.ofMinutes(15);

    @Override
    public void requestReset(String email) {

        // ⚠️ Sécurité : on ne révèle pas si email existe ou non
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return;

        String rawToken = UUID.randomUUID().toString();

        PasswordResetToken prt = PasswordResetToken.builder()
                .tokenHash(hashToken(rawToken))
                .expiresAt(Instant.now().plus(TOKEN_TTL))
                .used(false)
                .user(user)
                .build();

        tokenRepository.save(prt);

        // lien vers frontend (à adapter si front change d’URL)
        String resetLink = "http://localhost:5173/reset-password?token=" + rawToken;

        // Email simple (texte)
        mailService.sendPasswordReset(email, resetLink);
    }

    @Override
    public void resetPassword(String token, String newPassword) {

        String tokenHash = hashToken(token);

        PasswordResetToken prt = tokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Token invalide"));

        if (prt.isUsed()) {
            throw new IllegalArgumentException("Token déjà utilisé");
        }

        if (prt.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token expiré");
        }

        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        prt.setUsed(true);
        tokenRepository.save(prt);
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("Erreur hash token", e);
        }
    }
}
