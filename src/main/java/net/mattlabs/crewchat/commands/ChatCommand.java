package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.ConfigurateManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("chat|c")
@CommandPermission("crewchat.chat")
@Conditions("badconfig")
public class ChatCommand extends BaseCommand {

    private ChannelManager channelManager = CrewChat.getInstance().getChannelManager();
    private PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();
    private ConfigurateManager configurateManager = CrewChat.getInstance().getConfigurateManager();
    private PaperCommandManager paperCommandManager = CrewChat.getInstance().getPaperCommandManager();
    private BukkitAudiences platform = CrewChat.getInstance().getPlatform();
    private Chat chat = CrewChat.getChat();
    private Messages messages;

    public ChatCommand() {
        // Command Conditions
        paperCommandManager.getCommandConditions().addCondition("badconfig", (context -> {
            BukkitCommandIssuer issuer = context.getIssuer();
            if (issuer.isPlayer())
                if (!playerManager.playerExists(issuer.getPlayer())) {
                    platform.player(issuer.getPlayer()).sendMessage(messages.badConfig());
                    throw new ConditionFailedException("Bad config.");
                }
        }));

        // Command Completions
        paperCommandManager.getCommandCompletions().registerStaticCompletion("channels", channelManager.getChannelNames());

        messages = configurateManager.get("messages.conf");
    }

    @Default
    @Description("Chat base command.")
    public void onDefault(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            platform.player((Player) commandSender).sendMessage(messages.chatBaseCommand());
        }
        else CrewChat.getInstance().getLogger().info("Welcome to chat! (Run /chat help for help)");
    }

    @Subcommand("info")
    public class Info extends BaseCommand {

        // TODO: Clean up redundant code
        @Default
        @Description("Lists all channels, active channel and subscribed channels.")
        @CommandPermission("crewchat.chat.info")
        public void onInfo(CommandSender commandSender) {
            if (commandSender instanceof Player) {
                platform.player((Player) commandSender).sendMessage(messages.channelListHeader());
                for (Channel channel : channelManager.getChannels())
                    platform.player((Player) commandSender).sendMessage(messages.channelListEntry(channel.getName(), channel.getTextColor()));
                platform.player((Player) commandSender).sendMessage(messages.channelListActive(playerManager.getActiveChannel((Player) commandSender),
                        channelManager.channelFromString(playerManager.getActiveChannel((Player) commandSender)).getTextColor()));
                platform.player((Player) commandSender).sendMessage(messages.channelListSubscribedHeader());
                for (String channel : playerManager.getSubscribedChannels((Player) commandSender))
                    platform.player((Player) commandSender).sendMessage(messages.channelListEntry(channel, channelManager.channelFromString(channel).getTextColor()));
                if (!playerManager.getMutedPlayerNames((Player) commandSender).isEmpty()) {
                    platform.player((Player) commandSender).sendMessage(messages.mutedListHeader());
                    for (String mutee : playerManager.getMutedPlayerNames((Player) commandSender))
                        platform.player((Player) commandSender).sendMessage(messages.mutedListEntry(mutee));
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
                    platform.player((Player) commandSender).sendMessage(messages.channelInfo(requestedChannel.getName(),
                            requestedChannel.getTextColor().toString(),
                            requestedChannel.getTextColor()));
                }
                else CrewChat.getInstance().getLogger().info("Channel " + requestedChannel.getName()
                        + " info: " +
                        "\n - Name: " + requestedChannel.getName() +
                        "\n - Chat Color: " + requestedChannel.getTextColor().toString() +
                        "\n - Auto Subscribe: " + String.valueOf(requestedChannel.isAutoSubscribe()));
            } else {
                if (commandSender instanceof Player) {
                        platform.player((Player) commandSender).sendMessage(messages.channelNoExist(specifiedChannel));
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
                String   statusStr = String.join(" ", status);
                Player player = (Player) commandSender;
                playerManager.setStatus(player, statusStr);
                platform.player(player).sendMessage(messages.statusSet(statusStr));
        }
    }

    @Subcommand("subscribe")
    @Description("Subscribes player to channel.")
    @CommandCompletion("@channels")
    public void onSubscribe(CommandSender commandSender, String string) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            String channelName = null;
            if (channelManager.getChannels().contains(new Channel(string, null, false)))
                channelName = channelManager.channelFromString(string).getName();
            else {
                platform.player((Player) commandSender).sendMessage(messages.channelNoExist(string));
            }
            if (channelName != null) {
                if (commandSender.hasPermission("crewchat.chat.subscribe." + channelName)) {
                    if (playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        platform.player((Player) commandSender).sendMessage(messages.alreadySubscribed(channelName));
                    else {
                        playerManager.addSubscription((Player) commandSender, channelName);
                        platform.player((Player) commandSender).sendMessage(messages.nowSubscribed(channelName));
                    }
                }
                else {
                    platform.player((Player) commandSender).sendMessage(messages.noPermission());
                }
            }
            else platform.player((Player) commandSender).sendMessage(messages.cantSubscribe(string));
        }
    }

    // TODO: Change string name to something meaningful
    @Subcommand("unsubscribe")
    @Description("Unsubscribes player from channel.")
    @CommandCompletion("@channels")
    public void onUnsubscribe(CommandSender commandSender, String string) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            String channelName = null;
            if (channelManager.getChannels().contains(new Channel(string, null, false)))
                channelName = channelManager.channelFromString(string).getName();
            else {
                platform.player((Player) commandSender).sendMessage(messages.channelNoExist(string));
            }
            if (channelName != null) {
                if (commandSender.hasPermission("crewchat.chat.unsubscribe." + channelName)) {
                    if (!playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        platform.player((Player) commandSender).sendMessage(messages.notSubscribed(channelName));
                    else if (channelManager.channelFromString(playerManager.getActiveChannel((Player) commandSender))
                            .equals(channelManager.channelFromString(channelName)))
                        platform.player((Player) commandSender).sendMessage(messages.cantUnsubscribeActive(channelName));
                    else {
                        playerManager.removeSubscription((Player) commandSender, channelName);
                        platform.player((Player) commandSender).sendMessage(messages.nowUnsubscribed(channelName));
                    }
                }
                else {
                    platform.player((Player) commandSender).sendMessage(messages.noPermission());
                }
            }
            else platform.player((Player) commandSender).sendMessage(messages.cantUnsubscribe(string));
        }
    }
    
    @Subcommand("switch")
    @Description("Switches active channel.")
    @CommandCompletion("@channels")
    public void onSwitch(CommandSender commandSender, String string) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            String channelName = null;
            if (channelManager.getChannels().contains(new Channel(string, null, false)))
                channelName = channelManager.channelFromString(string).getName();
            else {
                platform.player((Player) commandSender).sendMessage(messages.channelNoExist(string));
            }
            if (channelName != null) {
                if (commandSender.hasPermission("crewchat.chat.switch." + channelName)) {
                    if (!playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        platform.player((Player) commandSender).sendMessage(messages.notSubscribed(channelName));
                    else {
                        playerManager.setActiveChannel((Player) commandSender, channelName);
                        platform.player((Player) commandSender).sendMessage(messages.newActiveChannel(channelName, channelManager.channelFromString(channelName).getTextColor()));
                    }
                }
                else {
                    platform.player((Player) commandSender).sendMessage(messages.noPermission());
                }
            }
            else {
                platform.player((Player) commandSender).sendMessage(messages.cantSetActive(string));
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
            // Check if player exists
            Player mutee = Bukkit.getPlayerExact(string);
            if (mutee == null) platform.player((Player) commandSender).sendMessage(messages.playerNoExist());
            // Check if muting self
            else if (commandSender.getName().equalsIgnoreCase(mutee.getName()))
                platform.player((Player) commandSender).sendMessage(messages.cantMuteSelf());
            // Check if mutee already muted
            else if (playerManager.getMutedPlayerNames((Player) commandSender).contains(string))
                platform.player((Player) commandSender).sendMessage(messages.playerAlreadyMuted(chat.getPlayerPrefix(mutee), mutee.getName()));
            else {
                playerManager.addMutedPlayer((Player) commandSender, mutee);
                platform.player((Player) commandSender).sendMessage(messages.playerMuted(chat.getPlayerPrefix(mutee), mutee.getName()));
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
            // Check if player exists
            Player mutee = Bukkit.getPlayerExact(string);
            if (mutee == null) platform.player((Player) commandSender).sendMessage(messages.playerNoExist());
            // Check if unmuting self
            else if (commandSender.getName().equalsIgnoreCase(mutee.getName()))
                platform.player((Player) commandSender).sendMessage(messages.cantUnmuteSelf());
            // Check if mutee already unmuted
            else if (playerManager.getMutedPlayerNames((Player) commandSender).contains(mutee.getName()))
                platform.player((Player) commandSender).sendMessage(messages.playerAlreadyUnmuted(chat.getPlayerPrefix(mutee), mutee.getName()));
            else {
                playerManager.removeMutedPlayer((Player) commandSender, mutee);
                platform.player((Player) commandSender).sendMessage(messages.playerUnmuted(chat.getPlayerPrefix(mutee), mutee.getName()));
            }
        }
    }

    @Subcommand("deafen")
    @Description("Surpresses all chat messages for player.")
    @CommandPermission("crewchat.chat.deafen")
    public void onDeafen(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            if (!playerManager.isDeafened((Player) commandSender)) {
                playerManager.setDeafened((Player) commandSender, true);
                platform.player((Player) commandSender).sendMessage(messages.playerDeafened());
            }
            else {
                playerManager.setDeafened((Player) commandSender, false);
                platform.player((Player) commandSender).sendMessage(messages.playerUndeafened());
            }
        }
    }

    @HelpCommand
    public void onHelp(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            platform.player((Player) commandSender).sendMessage(messages.chatHelpCommand());
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
