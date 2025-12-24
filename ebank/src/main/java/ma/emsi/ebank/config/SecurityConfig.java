package ma.emsi.ebank.config;

import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                // Activer CORS côté Spring Security
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:5173"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Laisser passer les préflights CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Auth publique
                        .requestMatchers("/api/auth/**").permitAll()

                        // Endpoints protégés
                        .requestMatchers(HttpMethod.POST, "/api/clients/**").hasRole("AGENT_GUICHET")
                        .requestMatchers(HttpMethod.GET, "/api/clients/**").hasRole("AGENT_GUICHET")
                        // Accounts protégés (AGENT_GUICHET)
                        .requestMatchers(HttpMethod.POST, "/api/accounts/**").hasRole("AGENT_GUICHET")
                        .requestMatchers(HttpMethod.GET,  "/api/accounts/**").hasRole("AGENT_GUICHET")
                        // Me (client)
                        .requestMatchers(HttpMethod.GET, "/api/me/**").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/transfers/**").hasRole("CLIENT")
                        //Client et agent change pwd
                        .requestMatchers(HttpMethod.POST, "/api/me/change-password").authenticated()

                        // Tout le reste = authentifié
                        .anyRequest().authenticated()
                )
                // Ajouter notre filtre JWT
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
