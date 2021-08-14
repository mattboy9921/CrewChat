package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.Mutee;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("chat|c")
@CommandPermission("crewchat.chat")
@Conditions("badconfig")
public class ChatCommand extends BaseCommand {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final ChannelManager channelManager = crewChat.getChannelManager();
    private final PlayerManager playerManager = crewChat.getPlayerManager();
    private final BukkitAudiences platform = crewChat.getPlatform();
    private final Chat chat = CrewChat.getChat();

    public ChatCommand() {
        PaperCommandManager paperCommandManager = CrewChat.getInstance().getPaperCommandManager();
        // Command Conditions
        paperCommandManager.getCommandConditions().addCondition("badconfig", (context -> {
            BukkitCommandIssuer issuer = context.getIssuer();
            if (issuer.isPlayer())
                if (!playerManager.playerExists(issuer.getPlayer())) {
                    platform.player(issuer.getPlayer()).sendMessage(crewChat.getMessages().badConfig());
                    throw new ConditionFailedException("Bad config.");
                }
        }));

        // Command Completions
        paperCommandManager.getCommandCompletions().registerStaticCompletion("channels", channelManager.getChannelNames());

        // Command Contexts
        paperCommandManager.getCommandContexts().registerContext(Channel.class, c -> new Channel(c.popFirstArg()));
    }

    @Default
    @Description("Chat base command.")
    public void onDefault(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            platform.player((Player) commandSender).sendMessage(crewChat.getMessages().chatBaseCommand());
        }
        else CrewChat.getInstance().getLogger().info("Welcome to chat! (Run /chat help for help)");
    }

    @Subcommand("info")
    public class Info extends BaseCommand {
        
        @Default
        @Description("Lists all channels, active channel and subscribed channels.")
        @CommandPermission("crewchat.chat.info")
        public void onInfo(CommandSender commandSender) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender; 
                platform.player(player).sendMessage(crewChat.getMessages().channelListHeader());
                for (Channel channel : channelManager.getChannels())
                    platform.player(player).sendMessage(crewChat.getMessages().channelListEntry(channel.getName(), channel.getTextColor()));
                platform.player(player).sendMessage(crewChat.getMessages().channelListActive(playerManager.getActiveChannel(player),
                        channelManager.channelFromString(playerManager.getActiveChannel(player)).getTextColor()));
                platform.player(player).sendMessage(crewChat.getMessages().channelListSubscribedHeader());
                for (String channel : playerManager.getSubscribedChannels(player))
                    platform.player(player).sendMessage(crewChat.getMessages().channelListEntry(channel, channelManager.channelFromString(channel).getTextColor()));
                if (!playerManager.getMutedPlayerNames(player).isEmpty()) {
                    platform.player(player).sendMessage(crewChat.getMessages().mutedListHeader());
                    for (Mutee mutee : playerManager.getMutedPlayers(player))
                        platform.player(player).sendMessage(crewChat.getMessages().mutedListEntry(mutee.getName(), mutee.getTimeRemaining()));
                }
            }
            else {
                CrewChat.getInstance().getLogger().info("Channel list: (Run /chat info channel [channel] for more info)");
                for (Channel channel : channelManager.getChannels())
                    CrewChat.getInstance().getLogger().info(" - " + channel.getName());
            }
        }

        @Subcommand("channel")
        @Description("Lists info about specified channel.")
        @CommandPermission("crewchat.chat.info.channel")
        @CommandCompletion("@channels")
        public void onChannel(CommandSender commandSender, String specifiedChannel) {
            Channel requestedChannel = null;
            for (Channel channel : channelManager.getChannels()) {
                if (channel.getName().equalsIgnoreCase(specifiedChannel)) requestedChannel = channel;
            }

            if (requestedChannel != null) {
                if (commandSender instanceof Player) {
                    platform.player((Player) commandSender).sendMessage(crewChat.getMessages().channelInfo(requestedChannel.getName(),
                            requestedChannel.getDescription(),
                            requestedChannel.getTextColor()));
                } else CrewChat.getInstance().getLogger().info("Channel " + requestedChannel.getName()
                        + " info: " +
                        "\n - Name: " + requestedChannel.getName() +
                        "\n - Chat Color: " + requestedChannel.getTextColor().toString() +
                        "\n - Auto Subscribe: " + requestedChannel.isAutoSubscribe());
            } else {
                if (commandSender instanceof Player) {
                        platform.player((Player) commandSender).sendMessage(crewChat.getMessages().channelNoExist(specifiedChannel));
                }
                else CrewChat.getInstance().getLogger().info("Channel " + specifiedChannel + " doesn't exist!");
            }
        }
    }

    @Subcommand("status")
    @Description("Sets player's status.")
    @CommandPermission("crewchat.chat.status")
    public void onStatus(CommandSender commandSender, String[] status) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender;
            if (status.length == 0) {
                try {
                    platform.player(player).sendMessage(crewChat.getMessages().statusIs(playerManager.getStatus(player)));
                }
                catch (ParsingException e) {
                    platform.player(player).sendMessage(crewChat.getMessages().statusSyntaxError());
                }
            }
            else {
                String statusStr = String.join(" ", status);
                // Make sure the status string doesn't have any syntax errors
                try {
                    platform.player(player).sendMessage(crewChat.getMessages().statusSet(statusStr));
                    playerManager.setStatus(player, statusStr);
                }
                catch (ParsingException e) {
                    platform.player(player).sendMessage(crewChat.getMessages().statusSyntaxError());
                }
            }
        }
    }

    @Subcommand("subscribe")
    @Description("Subscribes player to channel.")
    @CommandCompletion("@channels")
    public void onSubscribe(CommandSender commandSender, String string) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender; 
            String channelName = null;
            if (channelManager.getChannels().contains(new Channel(string)))
                channelName = channelManager.channelFromString(string).getName();
            else {
                platform.player(player).sendMessage(crewChat.getMessages().channelNoExist(string));
            }
            if (channelName != null) {
                if (player.hasPermission("crewchat.chat.subscribe." + channelName)) {
                    TextColor channelColor = channelManager.getTextColor(channelManager.channelFromString(channelName));
                    if (playerManager.getSubscribedChannels(player).contains(channelName))
                        platform.player(player).sendMessage(crewChat.getMessages().alreadySubscribed(channelName, channelColor));
                    else {
                        playerManager.addSubscription(player, channelName);
                        platform.player(player).sendMessage(crewChat.getMessages().nowSubscribed(channelName, channelColor));
                    }
                }
                else {
                    platform.player(player).sendMessage(crewChat.getMessages().noPermission());
                }
            }
            else platform.player(player).sendMessage(crewChat.getMessages().cantSubscribe(string, NamedTextColor.WHITE));
        }
    }
    
    @Subcommand("unsubscribe")
    @Description("Unsubscribes player from channel.")
    @CommandCompletion("@channels")
    public void onUnsubscribe(CommandSender commandSender, String string) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender; 
            String channelName = null;
            if (channelManager.getChannels().contains(new Channel(string)))
                channelName = channelManager.channelFromString(string).getName();
            else {
                platform.player(player).sendMessage(crewChat.getMessages().channelNoExist(string));
            }
            if (channelName != null) {
                if (player.hasPermission("crewchat.chat.unsubscribe." + channelName)) {
                    TextColor channelColor = channelManager.getTextColor(channelManager.channelFromString(channelName));
                    if (!playerManager.getSubscribedChannels(player).contains(channelName))
                        platform.player(player).sendMessage(crewChat.getMessages().notSubscribed(channelName, channelColor));
                    else if (channelManager.channelFromString(playerManager.getActiveChannel(player))
                            .equals(channelManager.channelFromString(channelName)))
                        platform.player(player).sendMessage(crewChat.getMessages().cantUnsubscribeActive(channelName, channelColor));
                    else {
                        playerManager.removeSubscription(player, channelName);
                        platform.player(player).sendMessage(crewChat.getMessages().nowUnsubscribed(channelName, channelColor));
                    }
                }
                else {
                    platform.player(player).sendMessage(crewChat.getMessages().noPermission());
                }
            }
            else platform.player(player).sendMessage(crewChat.getMessages().cantUnsubscribe(string, NamedTextColor.WHITE));
        }
    }
    
    @Subcommand("switch")
    @Description("Switches active channel.")
    @CommandCompletion("@channels")
    public void onSwitch(CommandSender commandSender, String string) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender; 
            String channelName = null;
            if (channelManager.getChannels().contains(new Channel(string)))
                channelName = channelManager.channelFromString(string).getName();
            else {
                platform.player(player).sendMessage(crewChat.getMessages().channelNoExist(string));
            }
            if (channelName != null) {
                if (player.hasPermission("crewchat.chat.switch." + channelName)) {
                    TextColor channelColor = channelManager.getTextColor(channelManager.channelFromString(channelName));
                    if (!playerManager.getSubscribedChannels(player).contains(channelName))
                        platform.player(player).sendMessage(crewChat.getMessages().notSubscribed(channelName, channelColor));
                    else {
                        playerManager.setActiveChannel(player, channelName);
                        platform.player(player).sendMessage(crewChat.getMessages().newActiveChannel(channelName, channelColor));
                    }
                }
                else {
                    platform.player(player).sendMessage(crewChat.getMessages().noPermission());
                }
            }
            else {
                platform.player(player).sendMessage(crewChat.getMessages().cantSetActive(string, NamedTextColor.WHITE));
            }
        }
    }

    @Subcommand("mute")
    @Description("Mutes another player.")
    @CommandCompletion("@players")
    @CommandPermission("crewchat.chat.mute")
    public void onMute(CommandSender commandSender, String string) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender; 
            // Check if player exists
            Player mutee = Bukkit.getPlayerExact(string);
            if (mutee == null) platform.player(player).sendMessage(crewChat.getMessages().playerNoExist());
            // Check if muting self
            else if (player.getName().equalsIgnoreCase(mutee.getName()))
                platform.player(player).sendMessage(crewChat.getMessages().cantMuteSelf());
            // Check if mutee already muted
            else if (playerManager.getMutedPlayerNames(player).contains(string))
                platform.player(player).sendMessage(crewChat.getMessages().playerAlreadyMuted(chat.getPlayerPrefix(mutee), mutee.getName()));
            else {
                playerManager.addMutedPlayer(player, mutee);
                platform.player(player).sendMessage(crewChat.getMessages().playerMuted(chat.getPlayerPrefix(mutee), mutee.getName()));
            }
        }
    }

    @Subcommand("unmute")
    @Description("Unmutes another player.")
    @CommandCompletion("@players")
    @CommandPermission("crewchat.chat.mute")
    public void onUnmute(CommandSender commandSender, String string) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender;
            // Check if unmuting self
            if (player.getName().equalsIgnoreCase(string))
                platform.player(player).sendMessage(crewChat.getMessages().cantUnmuteSelf());
            // Check if mutee already unmuted
            else if (!playerManager.getMutedPlayerNames(player).contains(string)) {
                // Check if mutee is online
                if (Bukkit.getPlayerExact(string) == null) platform.player(player).sendMessage(crewChat.getMessages().playerNoExist());
                else platform.player(player).sendMessage(crewChat.getMessages().playerAlreadyUnmuted(chat.getPlayerPrefix(Bukkit.getPlayerExact(string)), string));
            }
            // Mutee definitely muted
            else {
                // If mutee is offline
                if (Bukkit.getPlayerExact(string) == null) {
                    OfflinePlayer mutee = Bukkit.getOfflinePlayer(string);
                    playerManager.removeMutedPlayer(player, mutee);
                    platform.player(player).sendMessage(crewChat.getMessages().playerUnmuted(chat.getPlayerPrefix(player.getWorld().getName(), mutee), string));
                }
                // Mutee online
                else {
                    Player mutee = Bukkit.getPlayerExact(string);
                    playerManager.removeMutedPlayer(player, mutee);
                    platform.player(player).sendMessage(crewChat.getMessages().playerUnmuted(chat.getPlayerPrefix(mutee), string));
                }
            }
        }
    }

    @Subcommand("deafen")
    @Description("Suppresses all chat messages for player.")
    @CommandPermission("crewchat.chat.deafen")
    public void onDeafen(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender;
            if (!playerManager.isDeafened(player)) {
                playerManager.setDeafened(player, true);
                platform.player(player).sendMessage(crewChat.getMessages().playerDeafened());
            }
            else {
                playerManager.setDeafened(player, false);
                platform.player(player).sendMessage(crewChat.getMessages().playerUndeafened());
            }
        }
    }

    @Subcommand("send")
    @Description("Send a message to a specified channel without switching to it.")
    @CommandPermission("crewchat.chat.send")
    @CommandCompletion("@channels")
    public void onSend(CommandSender commandSender, Channel channel, String[] message) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender;
            // Check if channel is real
            if (!channelManager.getChannels().contains(channel))
                platform.player(player).sendMessage(crewChat.getMessages().channelNoExist(channel.getName()));
            // Check if player subscribed to channel
            else if (!playerManager.getSubscribedChannels(player).contains(channel.getName()))
                platform.player(player).sendMessage(crewChat.getMessages().notSubscribed(channel.getName(), channelManager.channelFromString(channel.getName()).getTextColor()));
            else
                crewChat.getChatSender().sendChatMessage(player, channel.getName(), String.join(" ", message));
        }
    }

    @HelpCommand
    public void onHelp(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            platform.player((Player) commandSender).sendMessage(crewChat.getMessages().chatHelpCommand());
        }
        else CrewChat.getInstance().getLogger().info("Command Help:\n" +
                "Alias: /c <args>\n" +
                "/chat - Base CrewChat command.\n" +
                "/chat help - Shows this screen.\n" +
                "/chat info - Lists all channels, active channel and subscribed channels.\n" +
                "/chat info channel <channel> - Lists info about specified channel.\n" +
                "Not available through console:\n" +
                "/chat status <status> - Sets player's status.\n" +
                "/chat subscribe <channel> - Subscribes player to channel.\n" +
                "/chat unsubscribe <channel> - Unsubscribes player from channel.\n" +
                "/chat switch <channel> - Switches active channel.");
    }
}
