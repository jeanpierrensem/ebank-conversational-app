package com.jp.ebankservice.mcp;

import com.jp.ebankservice.domain.*;
import com.jp.ebankservice.domain.Currency;
import com.jp.ebankservice.domain.Transaction;
import com.jp.ebankservice.dto.*;
import com.jp.ebankservice.services.*;
import org.springframework.ai.mcp.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class EbankMcpRestController {

    private final  EbankService ebankService;

    public EbankMcpRestController(EbankService ebankService) {
        this.ebankService = ebankService;
    }

    @McpTool(description = "get specific account by id")
    public AccountResponseDTO getAccountById(@McpArg(description = "account id") Long id) {
        return ebankService.getAccountById(id);
    }

    @McpTool(description = "get all accounts")
    public List<AccountResponseDTO> getAllAccounts() {
        return ebankService.getAllAccounts();
    }

    @McpTool(description = "get transaction by id")
    public Transaction getTransactionById(@McpArg(description = "transaction id")  String id) {
        return ebankService.getTransactionById(id);
    }

    @McpTool(description = "get all transactions")
    public List<Transaction> getAllTransactions() {
        return ebankService.getAllTransactions();
    }

    @McpTool(description = "get all transaction by account id")
    public List<Transaction> getAllTransactionsByAccountId(@McpArg(description = "the account id ")  Long id) {
        return ebankService.getAllTransactionsByAccountId(id);
    }


    @McpTool(description = "save a new account")
    public AccountResponseDTO saveAccount(
            @McpArg(description = "the initial balance")  double initialBalance,
            @McpArg(description = "the account currency")  Currency currency) {
        return ebankService.saveAccount(initialBalance, currency);
    }


    @McpTool(description = "credit an account ")
    public AccountResponseDTO credit(
            @McpArg(description = "the credited account id")  Long id,
            @McpArg(description = "the credited  amount ")  double amount) {
        return ebankService.credit(id, amount);
    }


    @McpTool(description = "debit an account")
    public AccountResponseDTO debit(
            @McpArg(description = "the debited account id")  Long id,
            @McpArg(description = "the debited amount") double amount) {
        return ebankService.debit(id, amount);
    }


}
