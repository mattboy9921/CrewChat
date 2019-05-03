package net.mattlabs.crewchat.commands;

import co.aikar.commands.*;
import co.aikar.commands.annotation.*;
import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("chat|c")
@CommandPermission("crewchat.chat")
@Conditions("badconfig")
public class ChatCommand extends BaseCommand {

    private ChannelManager channelManager = CrewChat.getInstance().getChannelManager();
    private PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();
    private PaperCommandManager paperCommandManager = CrewChat.getInstance().getPaperCommandManager();

    public ChatCommand() {
        // Command Conditions
        paperCommandManager.getCommandConditions().addCondition("badconfig", (context -> {
            BukkitCommandIssuer issuer = context.getIssuer();
            if (issuer.isPlayer())
                if (!playerManager.playerExists(issuer.getPlayer())) {
                    issuer.getPlayer().spigot().sendMessage(Messages.badConfig());
                    throw new ConditionFailedException("Bad config.");
                }
        }));

        // Command Completions
        paperCommandManager.getCommandCompletions().registerStaticCompletion("channels", channelManager.getChannelNames());
    }

    @Default
    @Description("Chat base command.")
    public void onDefault(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            commandSender.spigot().sendMessage(Messages.chatBaseCommand());
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
                commandSender.spigot().sendMessage(Messages.channelListHeader());
                for (Channel channel : channelManager.getChannels())
                    commandSender.spigot().sendMessage(Messages.channelListEntry(channel.getName(), channel.getChatColor()));
                commandSender.spigot().sendMessage(Messages.channelListActive(playerManager.getActiveChannel((Player) commandSender),
                        channelManager.channelFromString(playerManager.getActiveChannel((Player) commandSender)).getChatColor()));
                commandSender.spigot().sendMessage(Messages.channelListSubscribedHeader());
                for (String channel : playerManager.getSubscribedChannels((Player) commandSender))
                    commandSender.spigot().sendMessage(Messages.channelListEntry(channel, channelManager.channelFromString(channel).getChatColor()));
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
                        commandSender.spigot().sendMessage(Messages.channelInfo(requestedChannel.getName(),
                                requestedChannel.getChatColor().name(),
                                requestedChannel.getChatColor()));
                }
                else CrewChat.getInstance().getLogger().info("Channel " + requestedChannel.getName()
                        + " info: " +
                        "\n - Name: " + requestedChannel.getName() +
                        "\n - Chat Color: " + requestedChannel.getChatColor().name() +
                        "\n - Auto Subscribe: " + String.valueOf(requestedChannel.isAutoSubscribe()));
            } else {
                if (commandSender instanceof Player) {
                        commandSender.spigot().sendMessage(Messages.channelNoExist(specifiedChannel));
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
                commandSender.spigot().sendMessage(Messages.statusSet(statusStr));
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
                commandSender.spigot().sendMessage(Messages.channelNoExist(string));
            }
            if (channelName != null) {
                if (commandSender.hasPermission("crewchat.chat.subscribe." + channelName)) {
                    if (playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        commandSender.spigot().sendMessage(Messages.alreadySubscribed(channelName));
                    else {
                        playerManager.addSubscription((Player) commandSender, channelName);
                        commandSender.spigot().sendMessage(Messages.nowSubscribed(channelName));
                    }
                }
            }
            else commandSender.spigot().sendMessage(Messages.cantSubscribe(string));
        }
    }

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
                commandSender.spigot().sendMessage(Messages.channelNoExist(string));
            }
            if (channelName != null) {
                if (commandSender.hasPermission("crewchat.chat.unsubscribe." + channelName)) {
                    if (!playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        commandSender.spigot().sendMessage(Messages.notSubscribed(channelName));
                    else if (channelManager.channelFromString(playerManager.getActiveChannel((Player) commandSender))
                            .equals(channelManager.channelFromString(channelName)))
                        commandSender.spigot().sendMessage(Messages.cantUnsubscribeActive(channelName));
                    else {
                        playerManager.removeSubscription((Player) commandSender, channelName);
                        commandSender.spigot().sendMessage(Messages.nowUnsubscribed(channelName));
                    }
                }
            }
            else commandSender.spigot().sendMessage(Messages.cantUnsubscribe(string));
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
                commandSender.spigot().sendMessage(Messages.channelNoExist(string));
            }
            if (channelName != null) {
                if (commandSender.hasPermission("crewchat.chat.switch." + channelName)) {
                    if (!playerManager.getSubscribedChannels((Player) commandSender).contains(channelName))
                        commandSender.spigot().sendMessage(Messages.notSubscribed(channelName));
                    else {
                        playerManager.setActiveChannel((Player) commandSender, channelName);
                        commandSender.spigot().sendMessage(Messages.newActiveChannel(channelName, channelManager.channelFromString(channelName).getChatColor()));
                    }
                }
            }
            else {
                commandSender.spigot().sendMessage(Messages.cantSetActive(string));
            }
        }
    }

    @HelpCommand
    public void onHelp(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            commandSender.spigot().sendMessage(Messages.chatHelpCommand());
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
