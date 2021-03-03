package net.mattlabs.crewchat.listeners;

import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.ChatSender;
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
