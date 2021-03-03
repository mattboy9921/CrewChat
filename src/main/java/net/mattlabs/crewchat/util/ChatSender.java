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
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class ChatSender implements Runnable{

    private PlayerManager playerManager;
    private ChannelManager channelManager;
    private Chat chat;
    private String prefix, time, status, activeChannel, messageString;
    private Player player;
    private ArrayList<Player> subscribedPlayers, mentionedPlayers;
    private TextComponent message;

    public ChatSender(){
        playerManager = CrewChat.getInstance().getPlayerManager();
        channelManager = CrewChat.getInstance().getChannelManager();
        chat = CrewChat.getChat();
    }

    public void sendMessage(Player player, String message) {
        playerManager.updateMutedPlayers();

        this.player = player;
        if (playerManager.isOnline(player)) {
            messageString = message;
            prefix = colorize(chat.getPlayerPrefix(player));
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, HH:mm:ss");
            time = format.format(new Date());
            status = colorize(playerManager.getStatus(player));
            activeChannel = playerManager.getActiveChannel(player);
            subscribedPlayers = playerManager.getSubscribedPlayers(activeChannel);
            this.message = parseMessage(message, channelManager.getChatColor(channelManager.channelFromString(activeChannel)));
            CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), this);
        }
        else {
            player.sendMessage(Messages.badConfig());
            CrewChat.getInstance().getLogger().info("Player " + player.getDisplayName() + " can't send messages, check permissions!");
        }
    }

    public void run() {
        for (Player subbedPlayer : subscribedPlayers) {
            if (!playerManager.getMutedPlayerNames(subbedPlayer).contains(player.getName())) {
                for (Player mentionedPlayer : mentionedPlayers)
                    if (mentionedPlayer.equals(subbedPlayer)) {
                        subbedPlayer.playNote(subbedPlayer.getLocation(), Instrument.IRON_XYLOPHONE, Note.sharp(0, Note.Tone.C));
                        CrewChat.getInstance().getServer().getScheduler().runTaskLater(CrewChat.getInstance(), () -> {
                            subbedPlayer.playNote(subbedPlayer.getEyeLocation(), Instrument.IRON_XYLOPHONE, Note.sharp(1, Note.Tone.F));
                            CrewChat.getInstance().getServer().getScheduler().runTaskLater(CrewChat.getInstance(), () -> {
                                subbedPlayer.playNote(subbedPlayer.getEyeLocation(), Instrument.IRON_XYLOPHONE, Note.natural(1, Note.Tone.B));
                            }, 2);
                        }, 2);
                    }
                subbedPlayer.spigot().sendMessage(Messages.chatMessage(prefix,
                        player.getName(),
                        time,
                        status,
                        message,
                        activeChannel,
                        channelManager.getChatColor(channelManager.channelFromString(activeChannel))));
            }
        }
        CrewChat.getInstance().getLogger().info(TextComponent.toPlainText(Messages.chatMessage(prefix,
                player.getName(),
                time,
                status,
                message,
                activeChannel,
                channelManager.getChatColor(channelManager.channelFromString(activeChannel)))));
        if (CrewChat.getInstance().getDiscordSRVEnabled())
            DiscordUtil.sendMessage(DiscordSRV.getPlugin().getMainTextChannel(),
                    DiscordUtil.convertMentionsFromNames(TextComponent.toPlainText(Messages.chatMessage(prefix,
                            player.getName(),
                            time,
                            status,
                            message,
                            activeChannel,
                            channelManager.getChatColor(channelManager.channelFromString(activeChannel)))
                    ), DiscordSRV.getPlugin().getMainGuild()));
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
        mentionedPlayers = new ArrayList<>();

        for (String part : parts) {
            TextComponent nextComponent = new TextComponent(part);
            Player mentionedPlayer = null;

            // Match player names
            for (Player player : subscribedPlayers)
                if (Pattern.matches(player.getName() + ".?", part)) {
                    mentionedPlayers.add(player);
                    mentionedPlayer = player;
                }

            if (mentionedPlayer != null) {
                String[] mentionParts = part.split(mentionedPlayer.getName());
                nextComponent = new TextComponent("@" + mentionedPlayer.getName());
                nextComponent.setColor(ChatColor.GOLD);
                if (mentionParts.length > 0) {
                    TextComponent afterMention = new TextComponent(mentionParts[1]);
                    afterMention.setColor(chatColor);
                    nextComponent.addExtra(afterMention);
                }
            }
            // Match links
            else if (Pattern.matches("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?.*", part)) {
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


