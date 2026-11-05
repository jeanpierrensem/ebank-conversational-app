package com.jp.ebankservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString @EqualsAndHashCode
public class Transaction {
    @Id
    private String id;
    private double amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private LocalDateTime date;
    @ManyToOne
    private Account account;
}
