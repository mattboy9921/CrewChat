package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.ConfigManager;
import net.mattlabs.crewchat.util.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("crewchat|cc")
@CommandPermission("crewchat.use")
public class CrewChatCommand extends BaseCommand {

    ConfigManager configManager = CrewChat.getInstance().getConfigManager();
    ChannelManager channelManager = CrewChat.getInstance().getChannelManager();
    PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();

    @Default
    @Description("CrewChat base command.")
    public void onDefault(CommandSender commandSender) {
        if (commandSender instanceof Player) commandSender.spigot().sendMessage(Messages.crewChatBaseCommand());
        else CrewChat.getInstance().getLogger().info("Version " +
                Bukkit.getPluginManager().getPlugin("CrewChat").getDescription().getVersion() +
                ". For help, run '/crewchat help'");
    }

    @Subcommand("reload")
    @Description("Reloads CrewChat configuration files.")
    @CommandPermission("crewchat.reload")
    public void onReload(CommandSender commandSender) {
        configManager.reloadAllConfigs();
        channelManager.reloadChannels();
        playerManager.reloadPlayers();

        if (commandSender instanceof Player) commandSender.spigot().sendMessage(Messages.configReloaded());
        else CrewChat.getInstance().getLogger().info("Configuration reloaded.");
    }

    @Subcommand("help")
    @Description("CrewChat help command.")
    public void onHelp(CommandSender commandSender) {
        if (commandSender instanceof Player) commandSender.spigot().sendMessage(Messages.crewChatHelpCommand());
        else CrewChat.getInstance().getLogger().info("Command Help:\n"
                + "Alias: /cc <args>\n"
                + "/crewchat - Base CrewChat command.\n"
                + "/crewchat help - Shows this screen.\n"
                + "/crewchat reload - Reloads configuration files.");
    }
}
