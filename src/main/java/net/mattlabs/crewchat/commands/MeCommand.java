package net.mattlabs.crewchat.commands;

import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.MeSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeCommand implements CommandExecutor {

    MeSender meSender = CrewChat.getInstance().getMeSender();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            String   messageStr = String.join(" ", strings);
            meSender.sendMe((Player) commandSender, messageStr);
        }
        return true;
    }
}
