package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.*;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.parser.ParsingException;
import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.Mutee;
import net.mattlabs.crewchat.Party;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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
        // Mentionable players/users
        paperCommandManager.getCommandCompletions().registerAsyncCompletion("mentionable", c -> {
            Player player = c.getPlayer();
            String activeChannel = playerManager.getActiveChannel(player);

            // In game channel
            ArrayList<String> mentionable = new ArrayList<>();
            for (Player subbedPlayer : playerManager.getOnlineSubscribedPlayers(activeChannel)) mentionable.add(subbedPlayer.getName());

            // Discord channel
            if (crewChat.getDiscordSRVEnabled()) {
                if (channelManager.channelFromString(activeChannel).isExcludeFromDiscord()) {
                    TextChannel discordChannel;
                    if (DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(activeChannel) == null) discordChannel = DiscordSRV.getPlugin().getMainTextChannel();
                    else discordChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(activeChannel);

                    for (Member member : discordChannel.getMembers()) mentionable.add(member.getEffectiveName());
                }
            }
            return  mentionable;
        });
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
    @CommandPermission("crewchat.chat.info")
    public class Info extends BaseCommand {

        // TODO: Modify this method to compile list of channels and parties
        @Default
        @Description("Lists all channels, active channel and subscribed channels.")
        public void onInfo(CommandSender commandSender) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender; 
                platform.player(player).sendMessage(crewChat.getMessages().channelListHeader());
                for (Channel channel : channelManager.getChannels())
                    if (!(channel instanceof Party))
                        platform.player(player).sendMessage(crewChat.getMessages().channelListEntry(channel.getName(), channel.getTextColor()));
                if (channelManager.getChannelNames().stream().anyMatch(string -> channelManager.channelFromString(string) instanceof Party))
                    platform.player(player).sendMessage(crewChat.getMessages().partyListHeader());
                for (Channel channel : channelManager.getChannels())
                    if (channel instanceof Party)
                        platform.player(player).sendMessage(crewChat.getMessages().partyListEntry(channel.getName(), channel.getTextColor()));
                // TODO: Change this message to be channel/party specific
                platform.player(player).sendMessage(crewChat.getMessages().channelListActive(playerManager.getActiveChannel(player),
                        channelManager.channelFromString(playerManager.getActiveChannel(player)).getTextColor()));
                platform.player(player).sendMessage(crewChat.getMessages().channelListSubscribedHeader());
                for (String channel : playerManager.getSubscribedChannels(player))
                    if (!(channelManager.channelFromString(channel) instanceof Party))
                        platform.player(player).sendMessage(crewChat.getMessages().channelListEntry(channel, channelManager.channelFromString(channel).getTextColor()));
                if (playerManager.getSubscribedChannels(player).stream().anyMatch(string -> channelManager.channelFromString(string) instanceof Party))
                    platform.player(player).sendMessage(crewChat.getMessages().partyListJoinedHeader());
                for (String channel : playerManager.getSubscribedChannels(player))
                    if (channelManager.channelFromString(channel) instanceof Party)
                        platform.player(player).sendMessage(crewChat.getMessages().partyListEntry(channel, channelManager.channelFromString(channel).getTextColor()));
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
        @CommandCompletion("@channels")
        public void onChannel(CommandSender commandSender, Channel channel) {
            // Check if channel exists
            if (!channelManager.getChannels().contains(channel)) {
                if (commandSender instanceof Player)
                    platform.player((Player) commandSender).sendMessage(crewChat.getMessages().channelNoExist(channel.getName()));
                else CrewChat.getInstance().getLogger().info("Channel " + channel.getName() + " doesn't exist!");
            }
            else {
                Channel requestedChannel = channelManager.channelFromString(channel.getName());

                if (commandSender instanceof Player)
                    platform.player((Player) commandSender).sendMessage(crewChat.getMessages().channelInfo(requestedChannel.getName(),
                            requestedChannel.getDescription(),
                            requestedChannel.getTextColor()));
                else CrewChat.getInstance().getLogger().info("Channel " + requestedChannel.getName()
                        + " info: " +
                        "\n - Name: " + requestedChannel.getName() +
                        "\n - Chat Color: " + requestedChannel.getTextColor().toString() +
                        "\n - Auto Subscribe: " + requestedChannel.isAutoSubscribe());
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
    public void onSubscribe(CommandSender commandSender, Channel channel) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender;
            // Check if channel exists
            String channelName = null;
            if (channelManager.getChannels().contains(channel))
                channelName = channel.getName();
            else {
                platform.player(player).sendMessage(crewChat.getMessages().channelNoExist(channel.getName()));
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
            else platform.player(player).sendMessage(crewChat.getMessages().cantSubscribe(channel.getName(), NamedTextColor.WHITE));
        }
    }
    
    @Subcommand("unsubscribe")
    @Description("Unsubscribes player from channel.")
    @CommandCompletion("@channels")
    public void onUnsubscribe(CommandSender commandSender, Channel channel) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender;
            // Check if channel exists
            String channelName = null;
            if (channelManager.getChannels().contains(channel))
                channelName = channel.getName();
            else {
                platform.player(player).sendMessage(crewChat.getMessages().channelNoExist(channel.getName()));
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
            else platform.player(player).sendMessage(crewChat.getMessages().cantUnsubscribe(channel.getName(), NamedTextColor.WHITE));
        }
    }
    
    @Subcommand("switch")
    @Description("Switches active channel.")
    @CommandCompletion("@channels")
    public void onSwitch(CommandSender commandSender, Channel channel) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender;
            // Check if channel exists
            String channelName = null;
            if (channelManager.getChannels().contains(channel))
                channelName = channel.getName();
            else {
                platform.player(player).sendMessage(crewChat.getMessages().channelNoExist(channel.getName()));
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
                platform.player(player).sendMessage(crewChat.getMessages().cantSetActive(channel.getName(), NamedTextColor.WHITE));
            }
        }
    }

    @Subcommand("mute")
    @Description("Mutes another player.")
    @CommandCompletion("@players")
    @CommandPermission("crewchat.chat.mute")
    public void onMute(CommandSender commandSender, String muteeName) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender; 
            // Check if player exists
            Player mutee = Bukkit.getPlayerExact(muteeName);
            if (mutee == null) platform.player(player).sendMessage(crewChat.getMessages().playerNoExist());
            // Check if muting self
            else if (player.getName().equalsIgnoreCase(mutee.getName()))
                platform.player(player).sendMessage(crewChat.getMessages().cantMuteSelf());
            // Check if mutee already muted
            else if (playerManager.getMutedPlayerNames(player).contains(muteeName))
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
    public void onUnmute(CommandSender commandSender, String muteeName) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player player = (Player) commandSender;
            // Check if unmuting self
            if (player.getName().equalsIgnoreCase(muteeName))
                platform.player(player).sendMessage(crewChat.getMessages().cantUnmuteSelf());
            // Check if mutee already unmuted
            else if (!playerManager.getMutedPlayerNames(player).contains(muteeName)) {
                // Check if mutee is online
                if (Bukkit.getPlayerExact(muteeName) == null) platform.player(player).sendMessage(crewChat.getMessages().playerNoExist());
                else platform.player(player).sendMessage(crewChat.getMessages().playerAlreadyUnmuted(chat.getPlayerPrefix(Bukkit.getPlayerExact(muteeName)), muteeName));
            }
            // Mutee definitely muted
            else {
                // If mutee is offline
                if (Bukkit.getPlayerExact(muteeName) == null) {
                    OfflinePlayer mutee = Bukkit.getOfflinePlayer(muteeName);
                    playerManager.removeMutedPlayer(player, mutee);
                    platform.player(player).sendMessage(crewChat.getMessages().playerUnmuted(chat.getPlayerPrefix(player.getWorld().getName(), mutee), muteeName));
                }
                // Mutee online
                else {
                    Player mutee = Bukkit.getPlayerExact(muteeName);
                    playerManager.removeMutedPlayer(player, mutee);
                    platform.player(player).sendMessage(crewChat.getMessages().playerUnmuted(chat.getPlayerPrefix(mutee), muteeName));
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

    @Subcommand("mention")
    @Description("Mention a player in game or user on Discord.")
    @CommandPermission("crewchat.chat.mention")
    @CommandCompletion("@mentionable")
    public void onMention(CommandSender commandSender, String[] mentioned) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else crewChat.getChatSender().sendChatMessage((Player) commandSender, String.join(" ", mentioned));
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
