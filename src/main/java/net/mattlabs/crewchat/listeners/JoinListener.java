package net.mattlabs.crewchat.listeners;

import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class JoinListener implements Listener{

    private final PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();
    private final ChannelManager channelManager  = CrewChat.getInstance().getChannelManager();

    private boolean configError;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerManager.updateMutedPlayers();

        Player player = event.getPlayer();
        String activeChannel = null;
        for (Channel channel : channelManager.getChannels()) {
            if (player.hasPermission("crewchat.default.active." + channel.getName())) {
                activeChannel = channel.getName();
            }
        }
        if (!playerManager.playerExists(player)) {
            ArrayList<String> subscribedChannels = new ArrayList<>();
            for (Channel channel : channelManager.getChannels()) {
                if (channel.isAutoSubscribe()) subscribedChannels.add(channel.getName());
            }
            addPlayer(player, activeChannel, subscribedChannels);
            if (!configError) playerManager.setOnline(player);
        }
        else {
            playerManager.updateChannels(player);
            playerManager.setOnline(player);
            playerManager.setActiveChannel(player, activeChannel);
        }
    }

    private void addPlayer(Player player, String activeChannel, ArrayList<String> subscribedChannels) {
        try {
            playerManager.addPlayer(player, activeChannel, subscribedChannels);
            configError = false;
        }
        catch (NullPointerException e) {
            CrewChat.getInstance().getLogger().warning("Player " + player.getName() + " could not be added, check permissions!");
            configError = true;
        }
    }
}
