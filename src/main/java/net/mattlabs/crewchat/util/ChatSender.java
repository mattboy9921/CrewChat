package net.mattlabs.crewchat.util;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import github.scarsz.discordsrv.util.WebhookUtil;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.Party;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.Subst;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ChatSender implements Runnable{

    private final CrewChat crewChat = CrewChat.getInstance();
    private final PlayerManager playerManager = crewChat.getPlayerManager();
    private final ChannelManager channelManager = crewChat.getChannelManager();

    private final BukkitAudiences platform = crewChat.getPlatform();
    private final Chat chat = CrewChat.getChat();

    private String prefix, name, time, status, intendedChannel, discordHeader, discordChannelID;
    private Player player;
    private TextColor channelColor;
    @Subst("block.note_block.pling")
    private final String notificationSound;
    private ArrayList<Player> subscribedPlayers, mentionedPlayers;
    private Component message;
    private boolean allowColor, isDiscordMessage, excludeFromDiscord, isPartyMessage, showChannelName;

    public ChatSender(){
        // Check version for notification sound
        if (Versions.versionCompare("1.14.0", CrewChat.getInstance().getVersion()) <= 0)
            notificationSound = "block.note_block.iron_xylophone";
        else notificationSound = "block.note_block.pling";
    }

    // Get active channel from PlayerManager
    public void sendChatMessage(Player player, String message) {
        sendChatMessage(player, playerManager.getActiveChannel(player), message);
    }

    // Send a message originating from an in game channel
    @SuppressWarnings("deprecation")
    public void sendChatMessage(Player player, String intendedChannel, String message) {
        playerManager.updateMutedPlayers();
        isDiscordMessage = false;
        allowColor = player.hasPermission("crewchat.chat.color") || crewChat.getConfigCC().isAllowColor();

        // Check for configuration issue
        if (playerManager.isOnline(player)) {
            // Gather relavent info
            this.player = player;
            if (playerManager.isDeafened(player)) platform.player(player).sendMessage(crewChat.getMessages().chat().deafen().playerIsDeafened());
            prefix = chat.getPlayerPrefix(player);
            name = player.getName();
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, HH:mm:ss");
            time = format.format(new Date());
            status = playerManager.getStatus(player);
            this.intendedChannel = intendedChannel;
            if (crewChat.getDiscordSRVEnabled()) {
                if (DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(intendedChannel) == null) discordChannelID = DiscordSRV.getPlugin().getMainTextChannel().getId();
                else discordChannelID = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(intendedChannel).getId();
                excludeFromDiscord = channelManager.channelFromString(intendedChannel).isExcludeFromDiscord();
            }
            subscribedPlayers = playerManager.getOnlineSubscribedPlayers(intendedChannel);
            mentionedPlayers = MessageUtil.getMentionedPlayers(message, subscribedPlayers);
            channelColor = channelManager.getTextColor(channelManager.channelFromString(intendedChannel));
            isPartyMessage = channelManager.channelFromString(intendedChannel) instanceof Party;
            showChannelName = (isPartyMessage || channelManager.channelFromString(intendedChannel).isShowChannelNameInGame());
            this.message = MessageUtil.parseChatMessage(message, channelManager.getTextColor(channelManager.channelFromString(intendedChannel)), subscribedPlayers, discordChannelID, allowColor);
            // Schedule message to be sent
            CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), this);
        }
        else {
            platform.player(player).sendMessage(crewChat.getMessages().general().badConfig());
            CrewChat.getInstance().getLogger().info("Player " + player.getDisplayName() + " can't send messages, check permissions!");
        }
    }

    // Send a message originating from a Discord channel
    public void sendDiscordMessage(Member sender, TextChannel channel, String message) {
        isDiscordMessage = true;
        allowColor = false;

        // Gather info
        if (sender.getColor() == null) prefix = "<color:#ffffff>";
        else prefix = "<color:#" + Integer.toHexString(sender.getColor().getRGB()).substring(2) + ">";
        name = sender.getEffectiveName();
        discordHeader = crewChat.getConfigCC().isShowDiscordChannelNameInGame() ? "Discord #" + channel.getName() : "Discord";
        if (!sender.getActivities().isEmpty()) status = sender.getActivities().get(0).getName();
        else status = "No status";
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, HH:mm:ss");
        time = format.format(new Date());
        discordChannelID = channel.getId();

        // Loop through all in game channels linked to a Discord channel ID
        // Key is in game channel name, value is Discord channel ID
        Map<String, String> channelsMap = DiscordSRV.getPlugin().getChannels();
        int channelCount = 0;
        subscribedPlayers = new ArrayList<>();
        for (Map.Entry<String, String> channelEntry : channelsMap.entrySet()) {
            if (channelEntry.getValue().equals(channel.getId())) {
                channelCount++;
                for (Player player : playerManager.getOnlineSubscribedPlayers(channelEntry.getKey()))
                    if (!subscribedPlayers.contains(player)) {
                        subscribedPlayers.add(player);
                    }
            }
        }
        mentionedPlayers = MessageUtil.getMentionedPlayers(message, subscribedPlayers);

        if (channelCount > 1) {
            intendedChannel = "<color:#7289DA>(Discord)<reset> " + channel.getName();
            channelColor = NamedTextColor.WHITE;
            this.message = MessageUtil.parseChatMessage(message, NamedTextColor.WHITE, subscribedPlayers, discordChannelID, allowColor);
        }
        else {
            intendedChannel = channelManager.channelFromString(DiscordSRV.getPlugin().getDestinationGameChannelNameForTextChannel(channel)).getName();
            channelColor = channelManager.getTextColor(channelManager.channelFromString(intendedChannel));
            this.message = MessageUtil.parseChatMessage(message, channelManager.getTextColor(channelManager.channelFromString(intendedChannel)), subscribedPlayers, discordChannelID, allowColor);
        }

        // Schedule message to be sent
        CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), this);
    }

    // Handles actually sending messages to Discord/Minecraft
    public void run() {
        // Create messages
        Component messageComponent;
        if (isDiscordMessage) {
            messageComponent = crewChat.getMessages().chatMessage().discordMessage(discordHeader,
                    prefix,
                    name,
                    time,
                    status,
                    message,
                    intendedChannel,
                    channelColor);
        }
        else {
            messageComponent = crewChat.getMessages().chatMessage().chatMessage(prefix,
                    name,
                    time,
                    status,
                    message,
                    intendedChannel,
                    channelColor,
                    showChannelName,
                    isPartyMessage);
        }

        for (Player subbedPlayer : subscribedPlayers) {
            // Check if player has muted the sender or is deafened
            if (!playerManager.getMutedPlayerNames(subbedPlayer).contains(name) && !playerManager.isDeafened(subbedPlayer)) {
                // Send mentioned players a musical audio notification
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
                platform.player(subbedPlayer).sendMessage(messageComponent);
            }
        }
        platform.console().sendMessage(messageComponent);

        // Send the message on Discord
        if (!isDiscordMessage && !excludeFromDiscord)
            if (CrewChat.getInstance().getDiscordSRVEnabled())
                if (DiscordSRV.config().getBoolean("Experiment_WebhookChatMessageDelivery")) {
                    // Add channel name (if needed) to name
                    if (channelManager.channelFromString(intendedChannel).isShowChannelNameDiscord()) name = "[" + intendedChannel + "] " + name;
                    WebhookUtil.deliverMessage(DiscordUtil.getTextChannelById(discordChannelID),
                            name,
                            DiscordSRV.getAvatarUrl(player),
                            DiscordUtil.convertMentionsFromNames(MessageUtil.discordMarkdown(message), DiscordSRV.getPlugin().getMainGuild()),
                            null);
                } else {
                    // Add channel name (if needed) to message
                    String messageStrMD = channelManager.channelFromString(intendedChannel).isShowChannelNameDiscord() ? "[" + intendedChannel + "] " + MessageUtil.discordMarkdown(message) : MessageUtil.discordMarkdown(message);
                    DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(discordChannelID),
                            DiscordUtil.convertMentionsFromNames(messageStrMD, DiscordSRV.getPlugin().getMainGuild()));
                }
        // Reset fields
        prefix = null;
        name = null;
        discordHeader = null;
        time = null;
        status = null;
        message = null;
        channelColor = null;
        intendedChannel = null;
        discordChannelID = null;
        subscribedPlayers.clear();
        mentionedPlayers.clear();
    }
}


