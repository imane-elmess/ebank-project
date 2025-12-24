package ma.emsi.ebank.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String username;
    private String role;
    private String message;
    private String token;
}
