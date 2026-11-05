package com.jp.ebankservice.services;

import com.jp.ebankservice.Exceptions.*;
import com.jp.ebankservice.domain.*;
import com.jp.ebankservice.domain.Currency;
import com.jp.ebankservice.domain.Transaction;
import com.jp.ebankservice.repository.*;
import jakarta.transaction.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class EbankService {
    private final TransactionRepository transactionRepository;
    private final  AccountRepository accountRepository;

    public EbankService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    // Read Methods
    public Account getAccountById(Long id){
       return accountRepository.findById(id).orElseThrow(()-> new AccountNotFoundException("Account Not Found"));
   }
    public List<Account> getAllAccounts(){
       return accountRepository.findAll();
   }
    public  Transaction getTransactionById(String id){
       return transactionRepository.findById(id).orElseThrow(()-> new TransactionNotFoundException("Transaction Not Found"));
   }
    public List<Transaction> getAllTransactions(){
      return  transactionRepository.findAll();
   }
    public List<Transaction> getAllTransactionsByAccountId(Long id){
       Account account = getAccountById(id);
       return transactionRepository.findByAccount(account);
   }

    //write
    @Transactional
    public Account saveAccount(double initialBalance, Currency currency) {

        if(initialBalance < 0) {
            throw new NegativeBalanceException("The balance can not be negative");
        }

        final Account savedAccount = accountRepository.save(Account.builder()
                .balance(initialBalance)
                .type(Math.random() > 0.5 ? AccountType.SAVING_ACCOUNT : AccountType.CURRENT_ACCOUNT)
                .createAt(LocalDateTime.now())
                .status(AccountStatus.CREATED)
                .currency(currency)
                .build());
        final Transaction saveTransaction = transactionRepository.save(Transaction.builder()
                .date(LocalDateTime.now())
                        .id(UUID.randomUUID().toString())
                .account(savedAccount)
                .status(TransactionStatus.CREATED)
                .type(TransactionType.CREDIT)
                .amount(initialBalance)
                .build());
        return savedAccount;
    }


    @Transactional
    public Account credit(Long id, double amount) {

      Account account = getAccountById(id);
      account.setBalance(account.getBalance() + amount);
        final Transaction saveTransaction = transactionRepository.save(Transaction.builder()
                .date(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .account(account)
                .status(TransactionStatus.CREATED)
                .type(TransactionType.CREDIT)
                .amount(amount)
                .build());
        return accountRepository.save(account);

    }

    @Transactional
    public  Account debit(Long id, double amount) {

        if(amount < 0) {
            throw new NegativeBalanceException("The amount can not be negative");
        }
        Account account = getAccountById(id);
        account.setBalance(account.getBalance() - amount);
        final Transaction saveTransaction = transactionRepository.save(Transaction.builder()
                .date(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .account(account)
                .status(TransactionStatus.CREATED)
                .type(TransactionType.DEBIT)
                .amount(amount)
                .build());
        return accountRepository.save(account);

    }

    @Transactional
    public Account transfer(Long accountSourceId, Long accountDestinationId, double amount) {
        Account sourceAccount = debit(accountSourceId, amount);
        Account DestinationAccount = credit(accountDestinationId, amount);
        return sourceAccount;
    }
}
