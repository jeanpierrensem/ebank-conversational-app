package com.jp.ebankservice.repository;

import com.jp.ebankservice.domain.*;
import org.springframework.data.jpa.repository.*;

public interface AccountRepository  extends JpaRepository<Account, Long> {
}
