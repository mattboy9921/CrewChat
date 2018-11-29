package net.mattlabs.crewchat.listeners;

import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.ChatSender;
import net.mattlabs.crewchat.util.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    ChatSender chatSender = CrewChat.getInstance().getChatSender();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isAsynchronous()) {
            chatSender.sendMessage(event.getPlayer(), event.getMessage());
            event.setCancelled(true);
        }
    }
}
