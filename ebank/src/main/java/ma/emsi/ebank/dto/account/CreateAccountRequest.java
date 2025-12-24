package ma.emsi.ebank.dto.account;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    @NotNull
    private Long clientId;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal initialBalance;
}
