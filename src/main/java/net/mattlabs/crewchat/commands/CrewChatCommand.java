package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.ConfigurateManager;
import net.mattlabs.crewchat.util.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("crewchat|cc")
@CommandPermission("crewchat.use")
public class CrewChatCommand extends BaseCommand {

    private ConfigurateManager configurateManager = CrewChat.getInstance().getConfigurateManager();
    private ChannelManager channelManager = CrewChat.getInstance().getChannelManager();
    private PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();
    private BukkitAudiences platform = CrewChat.getInstance().getPlatform();
    private Messages messages;

    public CrewChatCommand() {
        messages = configurateManager.get("messages.conf");
    }

    @Default
    @Description("CrewChat base command.")
    public void onDefault(CommandSender commandSender) {
        if (commandSender instanceof Player) platform.player((Player) commandSender).sendMessage(messages.crewChatBaseCommand());
        else CrewChat.getInstance().getLogger().info("Version " +
                Bukkit.getPluginManager().getPlugin("CrewChat").getDescription().getVersion() +
                ". For help, run '/crewchat help'");
    }

    @Subcommand("reload")
    @Description("Reloads CrewChat configuration files.")
    @CommandPermission("crewchat.reload")
    public void onReload(CommandSender commandSender) {
        CrewChat.getInstance().getLogger().info("Reloading CrewChat...");
        configurateManager.reload();
        CrewChat.getInstance().getLogger().info("Configuration reloaded.");
        channelManager.reloadChannels();
        CrewChat.getInstance().getLogger().info("Channels reloaded.");
        playerManager.reloadPlayers();
        CrewChat.getInstance().getLogger().info("Players reloaded.");

        // TODO: Fix reload command, put in main class
        if (commandSender instanceof Player) platform.player((Player) commandSender).sendMessage(messages.configReloaded());
        CrewChat.getInstance().getLogger().info("CrewChat reloaded.");
    }

    @Subcommand("help")
    @Description("CrewChat help command.")
    public void onHelp(CommandSender commandSender) {
        if (commandSender instanceof Player) platform.player((Player) commandSender).sendMessage(messages.crewChatHelpCommand());
        else CrewChat.getInstance().getLogger().info("Command Help:\n"
                + "Alias: /cc <args>\n"
                + "/crewchat - Base CrewChat command.\n"
                + "/crewchat help - Shows this screen.\n"
                + "/crewchat reload - Reloads configuration files.");
    }
}
