package com.jp.ebankservice.rest;

import com.jp.ebankservice.domain.*;
import com.jp.ebankservice.dto.*;
import com.jp.ebankservice.services.*;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Controller
public class EbankGraphqlController {
EbankService service;

    public EbankGraphqlController(EbankService service) {
        this.service = service;
    }
    @QueryMapping
    public List<AccountResponseDTO> accountList() {
        return service.getAllAccounts();
    }

    @QueryMapping
    public AccountResponseDTO getAccountById(@Argument Long id) {
        return service.getAccountById(id);
    }

}
