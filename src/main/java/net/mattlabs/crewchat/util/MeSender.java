package net.mattlabs.crewchat.util;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MeSender implements Runnable {

    private PlayerManager playerManager;
    private ChannelManager channelManager;
    private ConfigurateManager configurateManager;
    private BukkitAudiences platform;
    private Messages messages;
    private String message, activeChannel;
    private Player player;
    private ArrayList<Player> subscribedPlayers;

    public MeSender() {
        playerManager = CrewChat.getInstance().getPlayerManager();
        channelManager = CrewChat.getInstance().getChannelManager();
        configurateManager = CrewChat.getInstance().getConfigurateManager();
        platform = CrewChat.getInstance().getPlatform();
        messages = configurateManager.get("messages.conf");
    }

    public void sendMe(Player player, String message) {
        playerManager.updateMutedPlayers();

        this.player = player;
        this.message = message;
        activeChannel = playerManager.getActiveChannel(player);
        subscribedPlayers = playerManager.getSubscribedPlayers(activeChannel);
        CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), this);
    }

    public void run() {
        for (Player subbedPlayer : subscribedPlayers)
            platform.player(subbedPlayer).sendMessage(messages.meMessage(player.getName(), message, channelManager.getChatColor(channelManager.channelFromString(activeChannel))));
        if (CrewChat.getInstance().getDiscordSRVEnabled())
            DiscordUtil.sendMessage(DiscordSRV.getPlugin().getMainTextChannel(), "_* " + player.getDisplayName() + " " + message + " *_");
        player = null;
        message = null;
        activeChannel = null;
        subscribedPlayers.clear();
    }
}
