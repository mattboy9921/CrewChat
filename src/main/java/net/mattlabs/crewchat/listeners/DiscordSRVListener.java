package net.mattlabs.crewchat.listeners;

import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import net.mattlabs.crewchat.CrewChat;
import org.bukkit.Bukkit;

public class DiscordSRVListener {

    @Subscribe(priority = ListenerPriority.MONITOR)
    public void gameChatMessageSent(GameChatMessagePreProcessEvent event) {
        CrewChat.getInstance().getLogger().info("Test");
    }

    @Subscribe(priority = ListenerPriority.MONITOR)
    public void discordMessageReceived(DiscordGuildMessageReceivedEvent event) {
        Bukkit.getLogger().info("Received a chat message on Discord: " + event.getMessage());
    }
}
