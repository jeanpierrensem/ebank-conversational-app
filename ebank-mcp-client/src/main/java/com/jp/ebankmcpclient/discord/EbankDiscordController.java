package com.jp.ebankmcpclient.discord;

import com.jp.ebankmcpclient.agent.*;
import com.zgamelogic.discord.annotations.*;
import net.dv8tion.jda.api.events.message.*;

@DiscordController
public class EbankDiscordController {
    private final EbankAgent ebankAgent;
    public EbankDiscordController(EbankAgent ebankAgent) {
        this.ebankAgent = ebankAgent;
    }


    @DiscordMapping
    private void perform(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        final String query = event.getMessage().getContentRaw();
        String response = ebankAgent.chat(query, "C1");
        event.getChannel().sendMessage(response).queue();

    }


}
