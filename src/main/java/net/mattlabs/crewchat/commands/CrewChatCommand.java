package net.mattlabs.crewchat.commands;

import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.ChannelManager;
import net.mattlabs.crewchat.util.ConfigManager;
import net.mattlabs.crewchat.util.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrewChatCommand implements CommandExecutor {

    ConfigManager configManager = CrewChat.getInstance().getConfigManager();
    ChannelManager channelManager = CrewChat.getInstance().getChannelManager();
    PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length == 0) {
            if (commandSender instanceof Player) {
                commandSender.spigot().sendMessage(Messages.crewChatBaseCommand());
            }
            else CrewChat.getInstance().getLogger().info("Version "
                    + Bukkit.getPluginManager().getPlugin("CrewChat").getDescription().getVersion()
                    + ". For help, run '/crewchat help'");
            return true;
        }
        else if (strings[0].equalsIgnoreCase("reload")) {
            if (commandSender instanceof Player) {
                if (commandSender.hasPermission("crewchat.reload")) {
                    configManager.reloadAllConfigs();
                    channelManager.reloadChannels();
                    playerManager.reloadPlayers();

                    commandSender.spigot().sendMessage(Messages.configReloaded());
                }
                else commandSender.spigot().sendMessage(Messages.noPermission());

            }
            else {
                configManager.reloadAllConfigs();
                channelManager.reloadChannels();
                playerManager.reloadPlayers();
            }
            CrewChat.getInstance().getLogger().info("Configuration reloaded.");
            return true;
        }
        else if (strings[0].equalsIgnoreCase("help")) {
            if (commandSender instanceof Player) {
                if (commandSender.hasPermission("crewchat.help")) {
                    commandSender.spigot().sendMessage(Messages.crewChatHelpCommand());
                }
                else commandSender.spigot().sendMessage(Messages.noPermission());
            }
            else CrewChat.getInstance().getLogger().info("Command Help:\n"
                    + "Alias: /cc <args>\n"
                    + "/crewchat - Base CrewChat command.\n"
                    + "/crewchat help - Shows this screen.\n"
                    + "/crewchat reload - Reloads configuration files.");
            return true;
        }
        else return false;
    }
}
