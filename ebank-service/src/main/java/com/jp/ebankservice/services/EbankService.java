package com.jp.ebankservice.services;

import com.jp.ebankservice.Exceptions.*;
import com.jp.ebankservice.domain.*;
import com.jp.ebankservice.domain.Currency;
import com.jp.ebankservice.domain.Transaction;
import com.jp.ebankservice.dto.*;
import com.jp.ebankservice.mappers.*;
import com.jp.ebankservice.repository.*;
import jakarta.transaction.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class EbankService {
    private final TransactionRepository transactionRepository;
    private final  AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public EbankService(TransactionRepository transactionRepository, AccountRepository accountRepository, AccountMapper accountMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }



    // Read Methods
    public AccountResponseDTO getAccountById(Long id){
        final Account accountNotFound = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account Not Found"));
        return accountMapper.fromAccountToAccountResponseDTO(accountNotFound);


    }
    public List<AccountResponseDTO> getAllAccounts(){

        return accountMapper.fromAccountToAccountResponseDTOs(accountRepository.findAll());


   }
    public  Transaction getTransactionById(String id){
       return transactionRepository.findById(id).orElseThrow(()-> new TransactionNotFoundException("Transaction Not Found"));

   }
    public List<Transaction> getAllTransactions(){
      return  transactionRepository.findAll();
   }
    public List<Transaction> getAllTransactionsByAccountId(Long id){
       Account account = accountRepository.getAccountById(id);
       return transactionRepository.findByAccount(account);
   }

    //write
    @Transactional
    public AccountResponseDTO saveAccount(double initialBalance, Currency currency) {

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
        AccountResponseDTO accountResponseDTO =  accountMapper.fromAccountToAccountResponseDTO(savedAccount);

        final Transaction saveTransaction = transactionRepository.save(Transaction.builder()
                .date(LocalDateTime.now())
                        .id(UUID.randomUUID().toString())
                .account(savedAccount)
                .status(TransactionStatus.CREATED)
                .type(TransactionType.CREDIT)
                .amount(initialBalance)
                .build());
        return accountResponseDTO;
    }


    @Transactional
    public AccountResponseDTO credit(Long id, double amount) {

      Account account = accountRepository.getAccountById(id);
      account.setBalance(account.getBalance() + amount);
        final Transaction saveTransaction = transactionRepository.save(Transaction.builder()
                .date(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .account(account)
                .status(TransactionStatus.CREATED)
                .type(TransactionType.CREDIT)
                .amount(amount)
                .build());
        final Account savedAccount = accountRepository.save(account);
        AccountResponseDTO accountResponseDTO =  accountMapper.fromAccountToAccountResponseDTO(savedAccount);
        return accountResponseDTO;

    }

    @Transactional
    public  AccountResponseDTO debit(Long id, double amount) {

        if(amount < 0) {
            throw new NegativeBalanceException("The amount can not be negative");
        }
        Account account = accountRepository.getAccountById(id);
        account.setBalance(account.getBalance() - amount);
        final Transaction saveTransaction = transactionRepository.save(Transaction.builder()
                .date(LocalDateTime.now())
                .id(UUID.randomUUID().toString())
                .account(account)
                .status(TransactionStatus.CREATED)
                .type(TransactionType.DEBIT)
                .amount(amount)
                .build());
        final Account savedAccount = accountRepository.save(account);
        AccountResponseDTO accountResponseDTO =  accountMapper.fromAccountToAccountResponseDTO(savedAccount);
        return accountResponseDTO;

    }

    @Transactional
    public AccountResponseDTO transfer(Long accountSourceId, Long accountDestinationId, double amount) {
        AccountResponseDTO  accountResponseDTO = debit(accountSourceId, amount);
        AccountResponseDTO  accountResponseDTODestination = credit(accountDestinationId, amount);
        return accountResponseDTO;
    }
}
