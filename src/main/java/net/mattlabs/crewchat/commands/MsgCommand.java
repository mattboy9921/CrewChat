package net.mattlabs.crewchat.commands;

import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.MsgManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MsgCommand implements CommandExecutor {

    MsgManager msgManager = CrewChat.getInstance().getMsgManager();
    PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();
    Chat chat = CrewChat.getChat();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            if (((Player) commandSender).getDisplayName().equalsIgnoreCase(strings[0])) {
                Messages.cantMessageSelf().send(commandSender);
            }
            else if (Bukkit.getPlayer(strings[0]) == null)
                Messages.playerNoExist().send(commandSender);
            else {
                Player recipient = Bukkit.getPlayer(strings[0]);
                String[] message    = Arrays.copyOfRange(strings, 1, strings.length);
                String messageStr = String.join(" ", message);
                msgManager.updatePlayer(strings[0], ((Player) commandSender).getName());
                Messages.privateMessageSend(chat.getPlayerPrefix((Player) commandSender),
                                            chat.getPlayerPrefix(recipient), strings[0],
                                            playerManager.getStatus((Player) commandSender),
                                            playerManager.getStatus(recipient), messageStr).send(commandSender);
                Messages.privateMessageReceive(chat.getPlayerPrefix((Player) commandSender),
                                            chat.getPlayerPrefix(recipient),
                                            ((Player) commandSender).getName(),
                                            playerManager.getStatus((Player) commandSender),
                                            playerManager.getStatus(recipient), messageStr).send(recipient);
            }
        }
        return true;
    }
}
