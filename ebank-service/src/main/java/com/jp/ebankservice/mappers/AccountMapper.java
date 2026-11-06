package com.jp.ebankservice.mappers;

import com.jp.ebankservice.domain.*;
import com.jp.ebankservice.dto.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class AccountMapper {

    public AccountRequestDTO fromAccountToAccountRequestDTO(Account account) {
        return AccountRequestDTO.builder()
                .type(account.getType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .build();
    }

    public List<AccountRequestDTO> fromAccountToAccountRequestDTOs(List<Account> accounts) {
        List<AccountRequestDTO>  accountResponseDTOS = new ArrayList<>();
        accounts.forEach(account -> {
                    accountResponseDTOS.add( AccountRequestDTO.builder()
                    .type(account.getType())
                    .balance(account.getBalance())
                    .currency(account.getCurrency())
                    .build());

        }
        );
        return  accountResponseDTOS;
    }

    public AccountResponseDTO fromAccountToAccountResponseDTO(Account account) {
        return AccountResponseDTO.builder()
                .id(account.getId())
                .status(account.getStatus())
                .createAt(account.getCreateAt())
                .type(account.getType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .build();
    }


    public List<AccountResponseDTO> fromAccountToAccountResponseDTOs(List<Account> accounts) {

        List<AccountResponseDTO>   accountResponseDTOS = new ArrayList<>();

        accounts.forEach(account -> {
            accountResponseDTOS.add(AccountResponseDTO.builder()
                    .id(account.getId())
                    .status(account.getStatus())
                    .createAt(account.getCreateAt())
                    .type(account.getType())
                    .balance(account.getBalance())
                    .currency(account.getCurrency())
                    .build());

        });

        return  accountResponseDTOS;

    }


}
