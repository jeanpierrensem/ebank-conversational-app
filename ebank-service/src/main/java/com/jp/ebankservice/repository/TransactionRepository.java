package com.jp.ebankservice.repository;

import com.jp.ebankservice.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
List<Transaction> findByAccount(Account account);
}
