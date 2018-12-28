package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.MsgManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandPermission("crewchat.pm")
public class MsgCommand extends BaseCommand {

    MsgManager msgManager = CrewChat.getInstance().getMsgManager();
    PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();
    Chat chat = CrewChat.getChat();

    @Default
    @CommandAlias("msg|pm|tell|whisper|w")
    @CommandCompletion("@players")
    @Description("Sends a private message to another player.")
    public void onDefault(CommandSender commandSender, Player player, String[] strings) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            if (((Player) commandSender).getDisplayName().equalsIgnoreCase(strings[0])) {
                commandSender.spigot().sendMessage(Messages.cantMessageSelf());
            }
            else if (player == null)
                commandSender.spigot().sendMessage(Messages.playerNoExist());
            else {
                Player recipient = player;
                String[] message    = Arrays.copyOfRange(strings, 1, strings.length);
                String messageStr = String.join(" ", message);
                msgManager.updatePlayer(strings[0], commandSender.getName());
                commandSender.spigot().sendMessage(Messages.privateMessageSend(
                        chat.getPlayerPrefix((Player) commandSender),
                        chat.getPlayerPrefix(recipient), strings[0],
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
