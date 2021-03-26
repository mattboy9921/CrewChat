package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.CrewChat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("crewchat|cc")
@CommandPermission("crewchat.use")
public class CrewChatCommand extends BaseCommand {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final BukkitAudiences platform = crewChat.getPlatform();

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

        // TODO: Fix reload command, put in main class
        if (commandSender instanceof Player) platform.player((Player) commandSender).sendMessage(crewChat.getMessages().configReloaded());
        CrewChat.getInstance().getLogger().info("CrewChat reloaded.");
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
