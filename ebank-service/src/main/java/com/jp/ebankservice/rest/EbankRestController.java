package com.jp.ebankservice.rest;

import com.jp.ebankservice.domain.*;
import com.jp.ebankservice.domain.Currency;
import com.jp.ebankservice.domain.Transaction;
import com.jp.ebankservice.services.*;
import jakarta.transaction.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class EbankRestController {

    private final EbankService ebankService;

    public EbankRestController(EbankService ebankService) {
        this.ebankService = ebankService;
    }

    @GetMapping("/accounts/{id}")
    public Account getAccountById(@PathVariable  Long id) {
        return ebankService.getAccountById(id);
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return ebankService.getAllAccounts();
    }

    @GetMapping("/transactions/{id}")
    public Transaction getTransactionById(@PathVariable String id) {
        return ebankService.getTransactionById(id);
    }
    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return ebankService.getAllTransactions();
    }

    @GetMapping("transactions/account/{id}")
    public List<Transaction> getAllTransactionsByAccountId(@PathVariable("id") Long accountId) {
        return ebankService.getAllTransactionsByAccountId(accountId);
    }

    @Transactional
    @PostMapping("/accounts/save")
    public Account saveAccount(double initialBalance, Currency currency) {
        return ebankService.saveAccount(initialBalance, currency);
    }

    @Transactional
    @PostMapping("/accounts/credit")
    public Account credit(Long id, double amount) {
        return ebankService.credit(id, amount);
    }

    @Transactional
    @PostMapping("/accounts/debit")
    public Account debit(Long id, double amount) {
        return ebankService.debit(id, amount);
    }

    @Transactional
    @PostMapping("/accounts/transfer")
    public Account transfer(Long sourceAccountId, Long destinationAccountId, double amount) {
        return ebankService.transfer(sourceAccountId, destinationAccountId, amount);
    }



}
