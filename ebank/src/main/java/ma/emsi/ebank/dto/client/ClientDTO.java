package ma.emsi.ebank.dto.client;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class ClientDTO {

    private Long id;
    private String identityNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String postalAddress;
    private Instant createdAt;
}
