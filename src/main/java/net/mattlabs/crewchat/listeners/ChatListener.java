package net.mattlabs.crewchat.listeners;

import github.scarsz.discordsrv.DiscordSRV;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.ChatSender;
import net.mattlabs.crewchat.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerChatPreviewEvent;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.ArrayList;

public class ChatListener implements Listener {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final ChatSender chatSender = crewChat.getChatSender();

    @EventHandler
    @SuppressWarnings("deprecation") // Paper API
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isAsynchronous()) {
            chatSender.sendChatMessage(event.getPlayer(), event.getMessage());
            event.setCancelled(true);
        }
        // Catch any plugin trying to send non-async chat messages
        else crewChat.getServer().getScheduler().runTaskAsynchronously(crewChat, () ->
                crewChat.getServer().getPluginManager().callEvent(
                new AsyncPlayerChatEvent(true, event.getPlayer(), event.getMessage(), event.getRecipients())));
    }

    // Cancel synchronous chat messages
    @EventHandler
    @SuppressWarnings("deprecation") // Paper API
    public void onPlayerSyncChat(PlayerChatEvent event) {
        event.setCancelled(true);
    }
}
