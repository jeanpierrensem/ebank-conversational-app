package com.jp.ebankservice;

import com.jp.ebankservice.domain.*;
import com.jp.ebankservice.repository.*;
import com.jp.ebankservice.services.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

@SpringBootApplication
public class EbankServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(EbankService ebankService, AccountRepository accountRepository) {
        return args -> {
            ebankService.saveAccount(250000, Currency.MAD);
            ebankService.saveAccount(500, Currency.EUR);
            ebankService.saveAccount(5000, Currency.USD);

            accountRepository.findAll().forEach(account -> {
                for (int i = 0; i < 10 ; i++) {
                    ebankService.credit(account.getId(), 500+ Math.random()*5000000);
                    ebankService.debit (account.getId(), 10+ Math.random()*500);
                }
            });
        };

    }
}
