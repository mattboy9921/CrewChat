package net.mattlabs.crewchat.util;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class ChatSender implements Runnable{

    private final PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();
    private final ChannelManager channelManager = CrewChat.getInstance().getChannelManager();

    private final Messages messages = CrewChat.getInstance().getMessages();
    private final BukkitAudiences platform = CrewChat.getInstance().getPlatform();
    private final Chat chat = CrewChat.getChat();

    private String prefix, time, status, activeChannel;
    private final String notificationSound;
    private Player player;
    private ArrayList<Player> subscribedPlayers, mentionedPlayers;
    private Component message;

    public ChatSender(){
        // Check version for notification sound
        if (Versions.versionCompare("1.14.0", CrewChat.getInstance().getVersion()) <= 0)
            notificationSound = "block.note_block.iron_xylophone";
        else notificationSound = "block.note_block.pling";
    }

    public void sendMessage(Player player, String message) {
        playerManager.updateMutedPlayers();

        this.player = player;
        if (playerManager.isOnline(player)) {
            if (playerManager.isDeafened(player)) platform.player(player).sendMessage(messages.playerIsDeafened());
            prefix = colorize(chat.getPlayerPrefix(player));
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, HH:mm:ss");
            time = format.format(new Date());
            status = colorize(playerManager.getStatus(player));
            activeChannel = playerManager.getActiveChannel(player);
            subscribedPlayers = playerManager.getSubscribedPlayers(activeChannel);
            this.message = parseMessage(message, channelManager.getTextColor(channelManager.channelFromString(activeChannel)));
            CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), this);
        }
        else {
            platform.player(player).sendMessage(messages.badConfig());
            CrewChat.getInstance().getLogger().info("Player " + player.getDisplayName() + " can't send messages, check permissions!");
        }
    }

    public void run() {
        Component messageComponent = messages.chatMessage(prefix,
                player.getName(),
                time,
                status,
                message,
                activeChannel,
                channelManager.getTextColor(channelManager.channelFromString(activeChannel)));

        for (Player subbedPlayer : subscribedPlayers) {
            if (!playerManager.getMutedPlayerNames(subbedPlayer).contains(player.getName()) && !playerManager.isDeafened(subbedPlayer)) {
                for (Player mentionedPlayer : mentionedPlayers)
                    if (mentionedPlayer.equals(subbedPlayer)) {
                        platform.player(subbedPlayer).playSound(Sound.sound(Key.key("minecraft", notificationSound), Sound.Source.PLAYER, 1f, (float) Math.pow(2f, -5f/12f))); // C#
                        CrewChat.getInstance().getServer().getScheduler().runTaskLater(CrewChat.getInstance(), () -> {
                            platform.player(subbedPlayer).playSound(Sound.sound(Key.key("minecraft", notificationSound), Sound.Source.PLAYER, 1f, 1f)); // F#
                            CrewChat.getInstance().getServer().getScheduler().runTaskLater(CrewChat.getInstance(), () -> {
                                platform.player(subbedPlayer).playSound(Sound.sound(Key.key("minecraft", notificationSound), Sound.Source.PLAYER, 1f, (float) Math.pow(2f, 5f/12f))); // B
                            }, 2);
                        }, 2);
                    }
                platform.player(subbedPlayer).sendMessage(parseMarkdown(messageComponent));
            }
        }
        platform.console().sendMessage(parseMarkdown(messageComponent));

        if (CrewChat.getInstance().getDiscordSRVEnabled())
            DiscordUtil.sendMessage(DiscordSRV.getPlugin().getMainTextChannel(),
                    DiscordUtil.convertMentionsFromNames(PlainComponentSerializer.plain().serialize(messageComponent),
                            DiscordSRV.getPlugin().getMainGuild()));
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

    private Component parseMessage(String message, TextColor textColor) {
        // Filter out any legacy codes/MiniMessage tags
        message = MiniMessage.get().serialize(LegacyComponentSerializer.legacy('&').deserialize(message));
        message = MiniMessage.get().serialize(LegacyComponentSerializer.legacy('§').deserialize(message));
        message = PlainComponentSerializer.plain().serialize(MiniMessage.get().parse(message));

        String[] parts = message.split(" ");
        Component componentMessage = MiniMessage.get().parse("");
        mentionedPlayers = new ArrayList<>();

        for (String part : parts) {
            Component nextComponent = MiniMessage.get().parse("<" + textColor.toString() + ">" + part);
            Player mentionedPlayer = null;

            // Match player names
            for (Player player : subscribedPlayers)
                if (Pattern.matches(player.getName() + ".?", part)) {
                    mentionedPlayers.add(player);
                    mentionedPlayer = player;
                }

            if (mentionedPlayer != null) {
                String[] mentionParts = part.split(mentionedPlayer.getName());
                nextComponent = MiniMessage.get().parse("<gold>@" + mentionedPlayer.getName());
                if (mentionParts.length > 0) {
                    Component afterMention = MiniMessage.get().parse("<" + textColor.toString() + ">" + mentionParts[1]);
                    nextComponent = nextComponent.append(afterMention);
                }
            }
            // Match links
            else if (Pattern.matches("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?.*", part)) {
                part = part.replaceAll("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?", "$0 ");
                String[] linkParts = part.split(" ");
                part = linkParts[0];
                if (!Pattern.matches("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$", part))
                    part = "http://" + part;
                nextComponent = MiniMessage.get().parse("<click:open_url:" + part + "><hover:show_text:'<white>Click to open link.'><blue>" + part);
                if (linkParts.length == 2) {
                    Component afterLink = MiniMessage.get().parse("<" + textColor.toString() + ">" + linkParts[1]);
                    nextComponent = nextComponent.append(afterLink);
                }
            }
            componentMessage = componentMessage.append(nextComponent);
            componentMessage = componentMessage.append(MiniMessage.get().parse(" "));
        }
        return componentMessage;
    }

    private Component parseMarkdown(Component message) {
        String messageSerialized = MiniMessage.get().serialize(message);
        return MiniMessage.withMarkdownFlavor(DiscordFlavor.get()).parse(messageSerialized);
    }
}


