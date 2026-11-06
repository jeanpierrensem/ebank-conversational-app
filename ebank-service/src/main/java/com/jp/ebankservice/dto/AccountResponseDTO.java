package com.jp.ebankservice.dto;
import com.jp.ebankservice.domain.*;
import lombok.*;
import java.time.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountResponseDTO {
    private Long id;
    private LocalDateTime createAt ;
    private double balance;
    private AccountType type ;
    private AccountStatus status;
    private Currency currency;
    private String customerId;
}
