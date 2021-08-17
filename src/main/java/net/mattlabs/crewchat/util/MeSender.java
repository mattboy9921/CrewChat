package net.mattlabs.crewchat.util;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import github.scarsz.discordsrv.util.WebhookUtil;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.mattlabs.crewchat.CrewChat;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MeSender implements Runnable {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final PlayerManager playerManager = crewChat.getPlayerManager();
    private final ChannelManager channelManager = crewChat.getChannelManager();

    private final BukkitAudiences platform = crewChat.getPlatform();

    private String message, intendedChannel;
    private Player player;
    private ArrayList<Player> subscribedPlayers;

    public void sendMe(Player player, String message) {
        playerManager.updateMutedPlayers();

        this.player = player;
        this.message = message;
        intendedChannel = playerManager.getActiveChannel(player);
        subscribedPlayers = playerManager.getOnlineSubscribedPlayers(intendedChannel);
        CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), this);
    }

    public void run() {
        // In game
        for (Player subbedPlayer : subscribedPlayers)
            platform.player(subbedPlayer).sendMessage(crewChat.getMessages().meMessage(player.getName(), message, channelManager.getTextColor(channelManager.channelFromString(intendedChannel))));
        // Discord
        if (crewChat.getDiscordSRVEnabled()) {
            if (!channelManager.channelFromString(intendedChannel).isExcludeFromDiscord()) {
                // Get channel ID
                String discordChannelID;
                if (DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(intendedChannel) == null) discordChannelID = DiscordSRV.getPlugin().getMainTextChannel().getId();
                else discordChannelID = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(intendedChannel).getId();
                // Send message
                DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(discordChannelID),
                        DiscordUtil.convertMentionsFromNames("_* " + player.getDisplayName() + " " + message + " *_", DiscordSRV.getPlugin().getMainGuild()));
            }
        }
        player = null;
        message = null;
        intendedChannel = null;
        subscribedPlayers.clear();
    }
}
