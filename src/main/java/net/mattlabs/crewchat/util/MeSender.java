package net.mattlabs.crewchat.util;

import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MeSender implements Runnable {

    private PlayerManager playerManager;
    private ChannelManager channelManager;
    private String message, activeChannel;
    private Player player;
    private ArrayList<Player> subscribedPlayers;

    public MeSender() {
        playerManager = CrewChat.getInstance().getPlayerManager();
        channelManager = CrewChat.getInstance().getChannelManager();
    }

    public void sendMe(Player player, String message) {
        this.player = player;
        this.message = message;
        activeChannel = playerManager.getActiveChannel(player);
        subscribedPlayers = playerManager.getSubscribedPlayers(activeChannel);
        CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), this);
    }

    public void run() {
        for (Player subbedPlayer : subscribedPlayers)
            subbedPlayer.spigot().sendMessage(Messages.meMessage(player.getDisplayName(), message, channelManager.getChatColor(channelManager.channelFromString(activeChannel))));
        player = null;
        message = null;
        activeChannel = null;
        subscribedPlayers.clear();
    }
}
