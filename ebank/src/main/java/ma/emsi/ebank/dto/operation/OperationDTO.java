package ma.emsi.ebank.dto.operation;

import lombok.Builder;
import lombok.Data;
import ma.emsi.ebank.enums.OperationType;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class OperationDTO {
    private Long id;
    private String description;
    private OperationType type;
    private BigDecimal amount;
    private Instant operationDate;
    private String counterpartRib;
    private String motif;
    private String reference;
}
