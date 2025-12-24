package ma.emsi.ebank.dto.client;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;


@Data
public class CreateClientRequest {

    @NotBlank private String identityNumber; //cin
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotNull  private LocalDate birthDate;
    @Email @NotBlank private String email;
    @NotBlank private String postalAddress;

}
