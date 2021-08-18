package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.BroadcastSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("broadcast|bc|yell")
@CommandPermission("crewchat.broadcast")
public class BroadcastCommand extends BaseCommand {

    BroadcastSender broadcastSender = CrewChat.getInstance().getBroadcastSender();

    @Default
    @Description("Sends message to all players.")
    public void onDefault(CommandSender commandSender, String message) {
        broadcastSender.sendBroadcast(message);
    }
}
