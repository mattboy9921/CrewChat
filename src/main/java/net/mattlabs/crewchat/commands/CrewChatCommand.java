package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("crewchat|cc")
@CommandPermission("crewchat.use")
public class CrewChatCommand extends BaseCommand {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final BukkitAudiences platform = crewChat.getPlatform();

    private final ChannelManager channelManager = crewChat.getChannelManager();
    private final PlayerManager playerManager = crewChat.getPlayerManager();

    @Default
    @Description("CrewChat base command.")
    public void onDefault(CommandSender commandSender) {
        if (commandSender instanceof Player) platform.player((Player) commandSender).sendMessage(crewChat.getMessages().crewChatBaseCommand());
        else CrewChat.getInstance().getLogger().info("Version " +
                CrewChat.getInstance().getDescription().getVersion() + ". For help, run '/crewchat help'");
    }

    @Subcommand("reload")
    @Description("Reloads CrewChat configuration files.")
    @CommandPermission("crewchat.reload")
    public void onReload(CommandSender commandSender) {
        CrewChat.getInstance().reload();

        if (commandSender instanceof Player) platform.player((Player) commandSender).sendMessage(crewChat.getMessages().configReloaded());
        CrewChat.getInstance().getLogger().info("CrewChat reloaded.");
    }

    @Subcommand("info")
    public class Info extends BaseCommand {

        @Default
        @Description("List all info for the CrewChat plugin.")
        @CommandPermission("crewchat.info")
        public void onInfo(CommandSender commandSender) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;

                // Header
                platform.player(player).sendMessage(crewChat.getMessages().crewChatInfoHeader());
                // Channels
                platform.player(player).sendMessage(crewChat.getMessages().crewChatChannelsLoaded(String.valueOf(channelManager.getChannels().size())));
                // Players
                platform.player(player).sendMessage(crewChat.getMessages().crewChatPlayersLoaded(String.valueOf(playerManager.getPlayerCount())));
                // Online Players
                platform.player(player).sendMessage(crewChat.getMessages().crewChatOnlinePlayersLoaded(String.valueOf(playerManager.getOnlinePlayerCount())));
                // Discord Integration
                platform.player(player).sendMessage(crewChat.getMessages().crewChatDiscordIntegration(crewChat.getDiscordSRVEnabled()));
            }
        }

        @Subcommand("channel")
        @Description("Lists info about specified channel.")
        @CommandPermission("crewchat.info.channel")
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
                    platform.player((Player) commandSender).sendMessage(crewChat.getMessages().crewChatChannelInfo(requestedChannel.getName(),
                            requestedChannel.getDescription(),
                            requestedChannel.getTextColor(),
                            playerManager.getSubscribedPlayers(requestedChannel.getName()).size(),
                            requestedChannel.isAutoSubscribe(),
                            requestedChannel.isExcludeFromDiscord(),
                            requestedChannel.isShowChannelNameDiscord()));
                else CrewChat.getInstance().getLogger().info("Channel " + requestedChannel.getName()
                        + " info: " +
                        "\n - Name: " + requestedChannel.getName() +
                        "\n - Chat Color: " + requestedChannel.getTextColor().toString() +
                        "\n - Subscribed players: " + playerManager.getSubscribedPlayers(requestedChannel.getName()).size() +
                        "\n - Auto Subscribe: " + (requestedChannel.isAutoSubscribe() ? "True" : "False") +
                        "\n - Exclude from Discord: " + (requestedChannel.isExcludeFromDiscord() ? "True" : "False") +
                        "\n - Show Channel Name on Discord: " + (requestedChannel.isShowChannelNameDiscord() ? "True" : "False"));
            }
        }
    }

    @Subcommand("help")
    @Description("CrewChat help command.")
    public void onHelp(CommandSender commandSender) {
        if (commandSender instanceof Player) platform.player((Player) commandSender).sendMessage(crewChat.getMessages().crewChatHelpCommand());
        else CrewChat.getInstance().getLogger().info("Command Help:\n"
                + "Alias: /cc <args>\n"
                + "/crewchat - Base CrewChat command.\n"
                + "/crewchat help - Shows this screen.\n"
                + "/crewchat reload - Reloads configuration files.");
    }
}
