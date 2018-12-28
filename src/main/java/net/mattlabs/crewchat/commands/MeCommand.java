package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.MeSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermission("crewchat.me")
public class MeCommand extends BaseCommand {

    MeSender meSender = CrewChat.getInstance().getMeSender();

    @Default
    @CommandAlias("me")
    @Description("Sends message in third person.")
    public void onDefault(CommandSender commandSender, @Optional String[] strings) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            String   messageStr = String.join(" ", strings);
            meSender.sendMe((Player) commandSender, messageStr);
        }
    }
}
