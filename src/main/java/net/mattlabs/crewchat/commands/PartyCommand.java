package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.PartyManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("party|p")
@CommandPermission("crewchat.party")
public class PartyCommand extends BaseCommand {

    private PartyManager partyManager = CrewChat.getInstance().getPartyManager();
    private PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();

    @Default
    @Description("Party base command.")
    public void onDefault(CommandSender commandSender) {
        if (commandSender instanceof Player) commandSender.spigot().sendMessage(Messages.partyBaseCommand());
        else CrewChat.getInstance().getLogger().info("Welcome to parties! (Run /parties help for help)");
    }

    @HelpCommand
    public void onHelp(CommandSender commandSender) {
        if (commandSender instanceof Player) commandSender.spigot().sendMessage(Messages.partyHelpCommand());
        else CrewChat.getInstance().getLogger().info("Command Help:\n" +
                "Alias: /p <args>\n" +
                "/party - Base CrewChat command.\n" +
                "/party help - Shows this screen.");
    }

    @Subcommand("create")
    public class Create extends BaseCommand {

        private String name;

        @Default
        @Description("Creates a party.")
        @CommandPermission("crewchat.party.create")
        public void onCreate(CommandSender commandSender, String name) {
            if (commandSender instanceof Player) {
                commandSender.spigot().sendMessage(Messages.createPartyName(name));
                commandSender.spigot().sendMessage(Messages.createPartyColor());
                this.name = name;
            }
            else CrewChat.getInstance().getLogger().info("Can't be run from console!");
        }

        @Subcommand("color")
        @Private
        public void onColor(CommandSender commandSender, String color) {
            if (commandSender instanceof Player) {
                ChatColor chatColor = null;
                boolean error = false;
                try {
                    chatColor = ChatColor.valueOf(color);
                }
                catch (IllegalArgumentException e) {
                    commandSender.spigot().sendMessage(Messages.partyColorInvalid());
                    error = true;
                }
                if (!error) {
                    partyManager.addParty(name, chatColor);
                    playerManager.addParty((Player) commandSender, name);
                    playerManager.setActiveChannel((Player) commandSender, name);
                    commandSender.spigot().sendMessage(Messages.partyCreated(name, chatColor));
                }
            }
        }
    }
}
