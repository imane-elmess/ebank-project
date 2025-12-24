package ma.emsi.ebank.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.dto.auth.ForgotPasswordRequest;
import ma.emsi.ebank.dto.auth.LoginRequest;
import ma.emsi.ebank.dto.auth.LoginResponse;
import ma.emsi.ebank.dto.auth.ResetPasswordRequest;
import ma.emsi.ebank.security.CustomUserDetails;
import ma.emsi.ebank.security.JwtService;
import ma.emsi.ebank.services.PasswordResetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    );

            Authentication authentication = authenticationManager.authenticate(authToken);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String username = userDetails.getUsername();
            String role = userDetails.getUser().getRole().name();

            //Générer le JWT
            String token = jwtService.generateToken(userDetails, role);

            // Construire la réponse avec le token
            LoginResponse response = LoginResponse.builder()
                    .username(username)
                    .role(role)
                    .message("Authentication successful")
                    .token(token)      //on ajoute le token ici
                    .build();

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {

            LoginResponse response = LoginResponse.builder()
                    .message("Invalid username or password")
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.requestReset(request.getEmail());
        // Toujours 204 pour ne pas révéler si l’email existe
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }


}
