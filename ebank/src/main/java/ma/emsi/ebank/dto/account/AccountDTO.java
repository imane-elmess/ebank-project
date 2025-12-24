package ma.emsi.ebank.dto.account;

import lombok.Builder;
import lombok.Data;
import ma.emsi.ebank.enums.AccountStatus;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class AccountDTO {
    private Long id;
    private String rib;
    private BigDecimal balance;
    private AccountStatus status;
    private Instant createdAt;
    private Instant lastMovementAt;
    private Long clientId;
}
