package net.mattlabs.crewchat.listeners;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import github.scarsz.discordsrv.api.events.DiscordReadyEvent;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.ChatSender;

public class DiscordSRVListener {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final ChatSender chatSender = crewChat.getChatSender();

    @Subscribe
    public void discordMessageReceivedPre(DiscordGuildMessagePreProcessEvent event) {
        chatSender.sendDiscordMessage(event.getMember(), event.getChannel(), event.getMessage().getContentDisplay());
        event.setCancelled(true);
    }

    @Subscribe
    public void discordReady(DiscordReadyEvent event) {
        // Check if DSV config set up correctly
        if (crewChat.getDiscordSRVEnabled())
            if (DiscordSRV.getPlugin().getMainTextChannel() == null) crewChat.setDiscordConfigError();
    }
}
