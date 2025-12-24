package ma.emsi.ebank.security;

import ma.emsi.ebank.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user; // on garde l'utilisateur réel à l'intérieur

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring attend des "authorities" ex: ROLE_CLIENT, ROLE_AGENT_GUICHET
        String roleName = "ROLE_" + user.getRole().name();
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // on renvoie le mot de passe encodé
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // ici on a choisi l'email comme username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // on ne gère pas l'expiration de compte pour le moment
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // on ne gère pas de blocage de compte pour l'instant
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // on ne gère pas l'expiration des credentials
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled(); // on utilise notre champ enabled
    }
}
