package com.jp.ebankservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @Builder @EqualsAndHashCode
public class Account {
    @Id @GeneratedValue
    private Long id;
    private LocalDateTime createAt ;
    private double balance;
    @Enumerated(EnumType.STRING)
    private AccountType type ;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    private Currency currency;
    private String customerId;
}
