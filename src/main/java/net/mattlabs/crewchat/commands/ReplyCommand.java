package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.MsgManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermission("crewchat.pm")
public class ReplyCommand extends BaseCommand {

    MsgManager msgManager = CrewChat.getInstance().getMsgManager();
    PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();
    Chat chat = CrewChat.getChat();

    @Default
    @CommandAlias("reply|r")
    @Description("Replies to last received private message.")
    public void onDefault(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            if (msgManager.playerExists(((Player) commandSender).getDisplayName())) {
                if (Bukkit.getPlayer(msgManager.getLastSender(((Player) commandSender).getDisplayName())) == null)
                    commandSender.spigot().sendMessage(Messages.playerNoExist());
                else {
                    String message = String.join(" ", strings);
                    Player recipient = Bukkit.getPlayer(msgManager.getLastSender(((Player) commandSender).getDisplayName()));
                    msgManager.updatePlayer(recipient.getDisplayName(), ((Player) commandSender).getDisplayName());
                    commandSender.spigot().sendMessage(Messages.privateMessageSend(
                            chat.getPlayerPrefix((Player) commandSender),
                            chat.getPlayerPrefix(recipient), recipient.getDisplayName(),
                            playerManager.getStatus((Player) commandSender),
                            playerManager.getStatus(recipient), message));
                    recipient.spigot().sendMessage(Messages.privateMessageReceive(
                            chat.getPlayerPrefix((Player) commandSender),
                            chat.getPlayerPrefix(recipient),
                            ((Player) commandSender).getDisplayName(),
                            playerManager.getStatus((Player) commandSender),
                            playerManager.getStatus(recipient), message));
                }
            }
            else commandSender.spigot().sendMessage(Messages.noPMReceived());
        }
    }
}
