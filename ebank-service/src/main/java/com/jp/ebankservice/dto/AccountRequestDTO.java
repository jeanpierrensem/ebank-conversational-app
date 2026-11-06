package com.jp.ebankservice.dto;


import com.jp.ebankservice.domain.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountRequestDTO {
    private double balance;
    private AccountType type ;
    private Currency currency;

}
