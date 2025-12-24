package ma.emsi.ebank.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.emsi.ebank.enums.OperationType;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "operations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Instant operationDate = Instant.now();

    // RIB du compte en face (pour un virement)
    private String counterpartRib;
    private String motif;
    // Référence commune pour les 2 opérations d'un même virement
    private String reference;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
