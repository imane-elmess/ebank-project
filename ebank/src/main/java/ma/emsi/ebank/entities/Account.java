package ma.emsi.ebank.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.emsi.ebank.enums.AccountStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 24)
    private String rib;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    private Instant createdAt = Instant.now();

    private Instant lastMovementAt;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client owner;

    @OneToMany(mappedBy = "account")
    private List<Operation> operations;
}
