package com.jp.ebankmcpclient.agent;

import org.springframework.ai.chat.client.*;
import org.springframework.ai.chat.client.advisor.*;
import org.springframework.ai.chat.memory.*;
import org.springframework.ai.tool.*;
import org.springframework.stereotype.*;


@Component
public class EbankAgent {

    private final ChatClient chatClient;



    public EbankAgent(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools, ChatMemory chatMemory) {

        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(tools)
                .build();
    }

    public String chat(String query, String ConversationId ) {
        return this.chatClient.prompt(query)
                .advisors( a-> a.param(ChatMemory.CONVERSATION_ID, ConversationId))
                .call().content();
    }
}
