package ma.emsi.ebank.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1️- Récupérer l'en-tête Authorization
        String authHeader = request.getHeader("Authorization");

        String jwt = null;
        String username = null;

        // 2️- vérifier qu'il commence par "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);

            try {
                username = jwtService.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                //RG_3
                response.getWriter().write("{\"message\":\"Session invalide, veuillez s’authentifier\"}");
                return;
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"Session invalide, veuillez s’authentifier\"}");
                return;
            }
        }

        // 3️- Si on a un username et qu'aucun utilisateur n'est encore authentifié dans ce contexte
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 4️- Charger l'utilisateur à partir de la base
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 5️- Vérifier que le token est valide pour cet utilisateur
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 6️- Construire un objet Authentication pour Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 7️- Enregistrer l'utilisateur authentifié dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8️- Passer la main au filtre suivant
        filterChain.doFilter(request, response);
    }
}
