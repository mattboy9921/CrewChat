package net.mattlabs.crewchat.util;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ChatSender implements Runnable{

    private PlayerManager playerManager;
    private ChannelManager channelManager;
    private Chat chat;
    private String prefix, status, message, activeChannel;
    private Player player;
    private ArrayList<Player> subscribedPlayers;

    public ChatSender(){
        playerManager = CrewChat.getInstance().getPlayerManager();
        channelManager = CrewChat.getInstance().getChannelManager();
        chat = CrewChat.getChat();
    }

    public void sendMessage(Player player, String message) {
        this.player = player;
        this.message = parseMessage(message);
        prefix = colorize(chat.getPlayerPrefix(player));
        status = colorize(playerManager.getStatus(player));
        activeChannel = playerManager.getActiveChannel(player);
        subscribedPlayers = playerManager.getSubscribedPlayers(activeChannel);
        CrewChat.getInstance().getServer().getScheduler().runTaskLater(CrewChat.getInstance(), this, 0);
    }

    public void run() {
        for (Player subbedPlayer : subscribedPlayers)
            //subbedPlayer.spigot()(Messages.chatMessage(prefix, player.getName(), status, message, activeChannel, channelManager.getChatColor(channelManager.channelFromString(activeChannel))));
              Messages.chatMessage(prefix, player.getName(), status, message, activeChannel, channelManager.getChatColor(channelManager.channelFromString(activeChannel))).send(subbedPlayer);
        CrewChat.getInstance().getLogger().info(player.getDisplayName() + ": " + message);
        //DiscordSRV.getPlugin().processChatMessage(player, message, activeChannel, false);
        if (CrewChat.getInstance().getDiscordSRVEnabled())
            DiscordUtil.sendMessage(DiscordSRV.getPlugin().getMainTextChannel(), prefix + player.getDisplayName() + "&r: " + message);
        prefix = null;
        player = null;
        status = null;
        message = null;
        activeChannel = null;
        subscribedPlayers.clear();
    }

    public static String colorize(String s){
        if(s == null) return null;
        return s.replaceAll("&([0-9a-f])", "\u00A7$1");
    }

    private String parseMessage(String message) {
        if (Pattern.matches("(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?", message))
            return message.replaceAll("(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?", "{\"text\":\"$1\",\"underlined\":true,\"color\":\"blue\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"$1\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Click to open link.\"}}");
        else return message;
    }
}


