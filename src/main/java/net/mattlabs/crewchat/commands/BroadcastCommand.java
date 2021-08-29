package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.BroadcastSender;
import org.bukkit.command.CommandSender;

@CommandAlias("broadcast|bc|yell")
@CommandPermission("crewchat.broadcast")
public class BroadcastCommand extends BaseCommand {

    BroadcastSender broadcastSender = CrewChat.getInstance().getBroadcastSender();

    @Default
    @Description("Sends message to all players.")
    @CommandCompletion("@nothing")
    public void onDefault(CommandSender commandSender, String message) {
        broadcastSender.sendBroadcast(message);
    }
}
