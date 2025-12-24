package ma.emsi.ebank.controllers.pwd;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.emsi.ebank.dto.auth.ChangePasswordRequest;
import ma.emsi.ebank.services.ChangePasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
@CrossOrigin(origins = "http://localhost:5173")
public class MeAuthController {

    private final ChangePasswordService changePasswordService;

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        changePasswordService.changePassword(request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build(); // 204
    }
}

