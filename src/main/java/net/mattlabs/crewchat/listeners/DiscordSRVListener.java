package net.mattlabs.crewchat.listeners;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.ChatSender;

public class DiscordSRVListener {

    private final ChatSender chatSender = CrewChat.getInstance().getChatSender();

    @Subscribe
    public void discordMessageReceivedPre(DiscordGuildMessagePreProcessEvent event) {
        chatSender.sendDiscordMessage(event.getMember(), event.getChannel(), event.getMessage().getContentDisplay());
    }
}
