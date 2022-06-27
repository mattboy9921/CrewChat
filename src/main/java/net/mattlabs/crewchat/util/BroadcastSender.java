package net.mattlabs.crewchat.util;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.CrewChat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastSender implements Runnable {

    private final CrewChat crewChat = CrewChat.getInstance();

    private final BukkitAudiences platform = crewChat.getPlatform();

    private String message;

    public void sendBroadcast(String message) {
        this.message = message;
        CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), this);
    }

    public void run() {
        for (Player player : Bukkit.getOnlinePlayers())
            platform.player(player).sendMessage(crewChat.getMessages().broadcastMessage(message));

        message = null;
    }
}
