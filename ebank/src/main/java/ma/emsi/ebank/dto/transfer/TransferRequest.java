package ma.emsi.ebank.dto.transfer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    @NotNull
    private Long fromAccountId;      //compte du client connecté

    @NotBlank
    private String toRib;            //rib destination

    @NotNull
    @DecimalMin(value = "1.00", inclusive = true)
    private BigDecimal amount;

    private String motif;            //optionnel
}
