package ma.emsi.ebank.security;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.entities.User;
import ma.emsi.ebank.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1️ Chercher l'utilisateur par username en base
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Utilisateur introuvable : " + username)
                );

        // 2️ L'adapter en CustomUserDetails pour Spring Security
        return new CustomUserDetails(user);
    }
}
