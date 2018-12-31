package net.mattlabs.crewchat.util;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ChatSender implements Runnable{

    private PlayerManager playerManager;
    private ChannelManager channelManager;
    private Chat chat;
    private String prefix, status, activeChannel, messageString;
    private Player player;
    private ArrayList<Player> subscribedPlayers;
    private TextComponent message;

    public ChatSender(){
        playerManager = CrewChat.getInstance().getPlayerManager();
        channelManager = CrewChat.getInstance().getChannelManager();
        chat = CrewChat.getChat();
    }

    public void sendMessage(Player player, String message) {
        this.player = player;
        messageString = message;
        prefix = colorize(chat.getPlayerPrefix(player));
        status = colorize(playerManager.getStatus(player));
        activeChannel = playerManager.getActiveChannel(player);
        subscribedPlayers = playerManager.getSubscribedPlayers(activeChannel);
        this.message = parseMessage(message, channelManager.getChatColor(channelManager.channelFromString(activeChannel)));
        CrewChat.getInstance().getServer().getScheduler().runTaskLater(CrewChat.getInstance(), this, 0);
    }

    public void run() {
        for (Player subbedPlayer : subscribedPlayers)
            subbedPlayer.spigot().sendMessage(Messages.chatMessage(prefix, player.getName(), status, message, activeChannel, channelManager.getChatColor(channelManager.channelFromString(activeChannel))));
            //subbedPlayer.spigot().sendMessage(message);
        CrewChat.getInstance().getLogger().info(player.getDisplayName() + ": " + messageString);
        //DiscordSRV.getPlugin().processChatMessage(player, message, activeChannel, false);
        if (CrewChat.getInstance().getDiscordSRVEnabled())
            DiscordUtil.sendMessage(DiscordSRV.getPlugin().getMainTextChannel(), prefix + player.getDisplayName() + "&r: " + messageString);
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

    private TextComponent parseMessage(String message, ChatColor chatColor) {
        String[] parts = message.split(" ");
        TextComponent componentMessage = new TextComponent("");
        for (String part : parts) {
            TextComponent nextComponent = new TextComponent(part);
            if (Pattern.matches("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?.*", part)) {
                part = part.replaceAll("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?", "$0 ");
                String[] linkParts = part.split(" ");
                part = linkParts[0];
                nextComponent = new TextComponent(part);
                nextComponent.setColor(ChatColor.BLUE);
                nextComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to open link.").create()));
                if (!Pattern.matches("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$", part))
                    nextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://" + part));
                else
                    nextComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, part));
                if (linkParts.length == 2) {
                    TextComponent afterLink = new TextComponent(linkParts[1]);
                    afterLink.setColor(chatColor);
                    nextComponent.addExtra(afterLink);
                }
            }
            else nextComponent.setColor(chatColor);
            componentMessage.addExtra(nextComponent);
            componentMessage.addExtra(" ");
        }
        return componentMessage;
    }
}


