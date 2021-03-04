package net.mattlabs.crewchat.listeners;

import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.ChatSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListener implements Listener {

    ChatSender chatSender = CrewChat.getInstance().getChatSender();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isAsynchronous()) {
            chatSender.sendMessage(event.getPlayer(), event.getMessage());
            event.setCancelled(true);
        }
        // Catch any plugin trying to send non-async chat messages
        else CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), () -> {
            CrewChat.getInstance().getServer().getPluginManager().callEvent(
                    new AsyncPlayerChatEvent(true, event.getPlayer(), event.getMessage(), event.getRecipients()));
        });
    }

    // Cancel synchronous chat messages
    @EventHandler
    public void onPlayerSyncChat(PlayerChatEvent event) {
        event.setCancelled(true);
    }
}
