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
import net.kyori.adventure.text.minimessage.ParsingException;
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
                    platform.player(issuer.getPlayer()).sendMessage(crewChat.getMessages().general().badConfig());
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
                if (!channelManager.channelFromString(activeChannel).isExcludeFromDiscord()) {
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
            platform.player((Player) commandSender).sendMessage(crewChat.getMessages().chat().base().chatBaseCommand());
        }
        else CrewChat.getInstance().getLogger().info("Welcome to chat! (Run /chat help for help)");
    }

    @Subcommand("info")
    @CommandPermission("crewchat.chat.info")
    public class Info extends BaseCommand {

        @Default
        @Description("Lists all channels, active channel and subscribed channels.")
        public void onInfo(CommandSender commandSender) {
            if (commandSender instanceof Player player) {
                // Channel List
                platform.player(player).sendMessage(crewChat.getMessages().chat().info().channelListHeader());
                for (Channel channel : channelManager.getChannels())
                    if (!(channel instanceof Party))
                        platform.player(player).sendMessage(crewChat.getMessages().chat().info().channelListEntry(channel.getName(), channel.getTextColor()));
                // Party List
                if (channelManager.getChannelNames().stream().anyMatch(string -> channelManager.channelFromString(string) instanceof Party))
                    platform.player(player).sendMessage(crewChat.getMessages().chat().info().partyListHeader());
                for (Channel channel : channelManager.getChannels())
                    if (channel instanceof Party)
                        platform.player(player).sendMessage(crewChat.getMessages().chat().info().partyListEntry(channel.getName(), channel.getTextColor()));
                // Active Channel
                platform.player(player).sendMessage(crewChat.getMessages().chat().info().channelListActive(playerManager.getActiveChannel(player),
                        channelManager.channelFromString(playerManager.getActiveChannel(player)).getTextColor()));
                // Subscribed Channel List
                platform.player(player).sendMessage(crewChat.getMessages().chat().info().channelListSubscribedHeader());
                for (String channel : playerManager.getSubscribedChannels(player))
                    if (!(channelManager.channelFromString(channel) instanceof Party))
                        platform.player(player).sendMessage(crewChat.getMessages().chat().info().channelListEntry(channel, channelManager.channelFromString(channel).getTextColor()));
                // Joined Party List
                if (playerManager.getSubscribedChannels(player).stream().anyMatch(string -> channelManager.channelFromString(string) instanceof Party))
                    platform.player(player).sendMessage(crewChat.getMessages().chat().info().partyListJoinedHeader());
                for (String channel : playerManager.getSubscribedChannels(player))
                    if (channelManager.channelFromString(channel) instanceof Party)
                        platform.player(player).sendMessage(crewChat.getMessages().chat().info().partyListEntry(channel, channelManager.channelFromString(channel).getTextColor()));
                // Muted Player List
                if (!playerManager.getMutedPlayerNames(player).isEmpty()) {
                    platform.player(player).sendMessage(crewChat.getMessages().chat().info().mutedListHeader());
                    for (Mutee mutee : playerManager.getMutedPlayers(player))
                        platform.player(player).sendMessage(crewChat.getMessages().chat().info().mutedListEntry(mutee.getName(), mutee.getTimeRemaining()));
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
        public void onChannel(CommandSender commandSender, @Single Channel channel) {
            // Check if channel exists
            if (channel == null) {
                platform.sender(commandSender).sendMessage(crewChat.getMessages().chat().general().channelNoExist(getLastCommandOperationContext().getArgs()[0]));
            }
            else {
                platform.sender(commandSender).sendMessage(crewChat.getMessages().chat().infoChannel().channelInfo(channel.getName(),
                        channel.getDescription(),
                        channel.getTextColor()));
            }
        }
    }

    @Subcommand("status")
    @Description("Sets player's status.")
    @CommandPermission("crewchat.chat.status")
    @CommandCompletion("@nothing")
    public void onStatus(Player player, @Optional String status) {
        // If no status given, display current status
        if (status == null) {
            try {
                platform.player(player).sendMessage(crewChat.getMessages().chat().status().statusIs(playerManager.getStatus(player)));
            }
            catch (ParsingException e) {
                platform.player(player).sendMessage(crewChat.getMessages().chat().status().statusSyntaxError());
            }
        }
        // Set new status
        else {
            // Make sure the status string doesn't have any syntax errors
            try {
                platform.player(player).sendMessage(crewChat.getMessages().chat().status().statusSet(status));
                playerManager.setStatus(player, status);
            }
            catch (ParsingException e) {
                platform.player(player).sendMessage(crewChat.getMessages().chat().status().statusSyntaxError());
            }
        }
    }

    @Subcommand("subscribe")
    @Description("Subscribes player to channel.")
    @CommandCompletion("@channels")
    public void onSubscribe(Player player, @Single Channel channel) {
        // Check if channel exists
        if (channel != null) {
            // Check permission
            if (player.hasPermission("crewchat.chat.subscribe." + channel.getName())) {
                TextColor channelColor = channel.getTextColor();
                // Check if already subscribed
                if (playerManager.getSubscribedChannels(player).contains(channel.getName()))
                    platform.player(player).sendMessage(crewChat.getMessages().chat().subscription().alreadySubscribed(channel.getName(), channelColor));
                else {
                    playerManager.addSubscription(player, channel.getName());
                    platform.player(player).sendMessage(crewChat.getMessages().chat().subscription().nowSubscribed(channel.getName(), channelColor));
                }
            }
            else {
                platform.player(player).sendMessage(crewChat.getMessages().general().noPermission());
            }
        }
        else platform.player(player).sendMessage(crewChat.getMessages().chat().subscription().cantSubscribe(getLastCommandOperationContext().getArgs()[0], NamedTextColor.WHITE));
    }
    
    @Subcommand("unsubscribe")
    @Description("Unsubscribes player from channel.")
    @CommandCompletion("@channels")
    public void onUnsubscribe(Player player, @Single Channel channel) {
        // Check if channel exists
        if (channel != null) {
            // Check permission
            if (player.hasPermission("crewchat.chat.unsubscribe." + channel.getName())) {
                TextColor channelColor = channel.getTextColor();
                // Check if already unsubscribed
                if (!playerManager.getSubscribedChannels(player).contains(channel.getName()))
                    platform.player(player).sendMessage(crewChat.getMessages().chat().subscription().notSubscribed(channel.getName(), channelColor));
                // Check if channel is active channel
                else if (channelManager.channelFromString(playerManager.getActiveChannel(player))
                        .equals(channelManager.channelFromString(channel.getName())))
                    platform.player(player).sendMessage(crewChat.getMessages().chat().subscription().cantUnsubscribeActive(channel.getName(), channelColor));
                else {
                    playerManager.removeSubscription(player, channel.getName());
                    platform.player(player).sendMessage(crewChat.getMessages().chat().subscription().nowUnsubscribed(channel.getName(), channelColor));
                }
            }
            else {
                platform.player(player).sendMessage(crewChat.getMessages().general().noPermission());
            }
        }
        else platform.player(player).sendMessage(crewChat.getMessages().chat().subscription().cantUnsubscribe(getLastCommandOperationContext().getArgs()[0], NamedTextColor.WHITE));
    }
    
    @Subcommand("switch")
    @CommandAlias("csw")
    @Description("Switches active channel.")
    @CommandCompletion("@channels|@parties")
    public void onSwitch(Player player, @Single Channel channel) {
        // Check if channel exists
        if (channel != null) {
            // Check if player has permission
            if (player.hasPermission("crewchat.chat.switch." + channel.getName()) || channel instanceof Party) {
                TextColor channelColor = channel.getTextColor();
                // Check if player subscribed to channel
                if (!playerManager.getSubscribedChannels(player).contains(channel.getName()))
                    platform.player(player).sendMessage(crewChat.getMessages().chat().subscription().notSubscribed(channel.getName(), channelColor));
                else {
                    playerManager.setActiveChannel(player, channel.getName());
                    platform.player(player).sendMessage(crewChat.getMessages().chat().svitch().newActiveChannel(channel.getName(), channelColor));
                }
            }
            else {
                platform.player(player).sendMessage(crewChat.getMessages().general().noPermission());
            }
        }
        else {
            platform.player(player).sendMessage(crewChat.getMessages().chat().svitch().cantSetActive(getLastCommandOperationContext().getArgs()[0], NamedTextColor.WHITE));
        }
    }

    @Subcommand("mute")
    @Description("Mutes another player.")
    @CommandPermission("crewchat.chat.mute")
    @CommandCompletion("@players")
    @SuppressWarnings("ConstantConditions")
    public void onMute(Player player, @Single String mutee) {
        // Check if player exists
        if (!playerManager.playerExists(Bukkit.getOfflinePlayer(mutee))) platform.player(player).sendMessage(crewChat.getMessages().privateMessage().playerNoExist());
        // Check if muting self
        else if (player.getName().equalsIgnoreCase(mutee))
            platform.player(player).sendMessage(crewChat.getMessages().chat().muting().cantMuteSelf());
        // Check if mutee already muted
        else if (playerManager.getMutedPlayerNames(player).contains(mutee))
            platform.player(player).sendMessage(crewChat.getMessages().chat().muting().playerAlreadyMuted(chat.getPlayerPrefix(Bukkit.getPlayerExact(mutee)), mutee));
        else {
            playerManager.addMutedPlayer(player, Bukkit.getPlayerExact(mutee)); // Never will be null, checked in first if
            platform.player(player).sendMessage(crewChat.getMessages().chat().muting().playerMuted(chat.getPlayerPrefix(Bukkit.getPlayerExact(mutee)), mutee));
        }
    }

    @Subcommand("unmute")
    @Description("Unmutes another player.")
    @CommandPermission("crewchat.chat.mute")
    @CommandCompletion("@players")
    @SuppressWarnings("ConstantConditions")
    public void onUnmute(Player player, @Single String mutee) {
        // Check if unmuting self
        if (player.getName().equalsIgnoreCase(mutee))
            platform.player(player).sendMessage(crewChat.getMessages().chat().muting().cantUnmuteSelf());
        // Check if mutee already unmuted
        else if (!playerManager.getMutedPlayerNames(player).contains(mutee)) {
            // Check if mutee is online
            if (Bukkit.getPlayerExact(mutee) == null) platform.player(player).sendMessage(crewChat.getMessages().privateMessage().playerNoExist());
            else platform.player(player).sendMessage(crewChat.getMessages().chat().muting().playerAlreadyUnmuted(chat.getPlayerPrefix(Bukkit.getPlayerExact(mutee)), mutee));
        }
        // Mutee definitely muted
        else {
            // If mutee is offline
            if (Bukkit.getPlayerExact(mutee) == null) {
                OfflinePlayer muteePlayer = Bukkit.getOfflinePlayer(mutee);
                playerManager.removeMutedPlayer(player, muteePlayer);
                platform.player(player).sendMessage(crewChat.getMessages().chat().muting().playerUnmuted(chat.getPlayerPrefix(player.getWorld().getName(), muteePlayer), mutee));
            }
            // Mutee online
            else {
                Player muteePlayer = Bukkit.getPlayerExact(mutee);
                playerManager.removeMutedPlayer(player, muteePlayer);
                platform.player(player).sendMessage(crewChat.getMessages().chat().muting().playerUnmuted(chat.getPlayerPrefix(muteePlayer), mutee));
            }
        }
    }

    @Subcommand("deafen")
    @Description("Suppresses all chat messages for player.")
    @CommandPermission("crewchat.chat.deafen")
    public void onDeafen(Player player) {
        // Check if already deafened
        if (!playerManager.isDeafened(player)) {
            playerManager.setDeafened(player, true);
            platform.player(player).sendMessage(crewChat.getMessages().chat().deafen().playerDeafened());
        }
        else {
            playerManager.setDeafened(player, false);
            platform.player(player).sendMessage(crewChat.getMessages().chat().deafen().playerUndeafened());
        }
    }

    @Subcommand("send")
    @CommandAlias("cs")
    @Description("Send a message to a specified channel without switching to it.")
    @CommandPermission("crewchat.chat.send")
    @CommandCompletion("@channels|@parties @nothing")
    public void onSend(Player player, Channel channel, String message) {
        // Check if channel is real
        if (channel == null)
            platform.player(player).sendMessage(crewChat.getMessages().chat().general().channelNoExist(getLastCommandOperationContext().getArgs()[0]));
        // Check if player subscribed to channel
        else if (!playerManager.getSubscribedChannels(player).contains(channel.getName()))
            platform.player(player).sendMessage(crewChat.getMessages().chat().subscription().notSubscribed(channel.getName(), channelManager.channelFromString(channel.getName()).getTextColor()));
        else
            crewChat.getChatSender().sendChatMessage(player, channel.getName(), message);
    }

    @Subcommand("mention")
    @Description("Mention a player in game or user on Discord.")
    @CommandPermission("crewchat.chat.mention")
    @CommandCompletion("@mentionable")
    public void onMention(Player player, String mentioned) {
        crewChat.getChatSender().sendChatMessage(player, mentioned);
    }

    @HelpCommand
    public void onHelp(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            platform.player((Player) commandSender).sendMessage(crewChat.getMessages().chat().help().chatHelpCommand());
        }
        else CrewChat.getInstance().getLogger().info("""
                Command Help:
                Alias: /c <args>
                /chat - Base CrewChat command.
                /chat help - Shows this screen.
                /chat info - Lists all channels, active channel and subscribed channels.
                /chat info channel <channel> - Lists info about specified channel.
                Not available through console:
                /chat status <status> - Sets player's status.
                /chat subscribe <channel> - Subscribes player to channel.
                /chat unsubscribe <channel> - Unsubscribes player from channel.
                /chat switch <channel> - Switches active channel.""");
    }
}
