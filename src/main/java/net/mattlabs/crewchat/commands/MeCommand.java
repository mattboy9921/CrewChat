package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.MeSender;
import org.bukkit.entity.Player;

@CommandAlias("me")
@CommandPermission("crewchat.me")
public class MeCommand extends BaseCommand {

    MeSender meSender = CrewChat.getInstance().getMeSender();

    @Default
    @Description("Sends message in third person.")
    @CommandCompletion("@nothing")
    public void onDefault(Player player, String message) {
        meSender.sendMe(player, message);
    }
}