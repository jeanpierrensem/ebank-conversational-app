package com.jp.ebankmcpclient.rest;

import com.jp.ebankmcpclient.agent.*;
import org.springframework.ai.tool.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class EbankMcpRestController {
private final EbankAgent ebankAgent ;

    public EbankMcpRestController(EbankAgent ebankAgent) {
        this.ebankAgent = ebankAgent;
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(String query){
        return ebankAgent.chat(query, "C1");
    }
}

