package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.MsgManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandAlias("msg|pm|tell|whisper|w")
@CommandPermission("crewchat.pm")
public class MsgCommand extends BaseCommand {

    MsgManager msgManager = CrewChat.getInstance().getMsgManager();
    PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();
    Chat chat = CrewChat.getChat();

    @Default
    @CommandCompletion("@players @nothing")
    @Description("Sends a private message to another player.")
    public void onDefault(CommandSender commandSender, String recipientString, String[] message) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            Player recipient = Bukkit.getPlayerExact(recipientString);
            if (recipient == null) commandSender.spigot().sendMessage(Messages.playerNoExist());
            else if (((Player) commandSender).getDisplayName().equalsIgnoreCase(recipient.getDisplayName())) {
                commandSender.spigot().sendMessage(Messages.cantMessageSelf());
            }
            else {
                String messageStr = String.join(" ", message);
                msgManager.updatePlayer(recipient.getDisplayName(), commandSender.getName());
                commandSender.spigot().sendMessage(Messages.privateMessageSend(
                        chat.getPlayerPrefix((Player) commandSender),
                        chat.getPlayerPrefix(recipient), recipient.getDisplayName(),
                        playerManager.getStatus((Player) commandSender),
                        playerManager.getStatus(recipient), messageStr));
                recipient.spigot().sendMessage(Messages.privateMessageReceive(
                        chat.getPlayerPrefix((Player) commandSender),
                        chat.getPlayerPrefix(recipient),
                        commandSender.getName(),
                        playerManager.getStatus((Player) commandSender),
                        playerManager.getStatus(recipient), messageStr));
            }
        }
    }
}
