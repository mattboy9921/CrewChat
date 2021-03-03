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

@CommandAlias("reply|r")
@CommandPermission("crewchat.pm")
public class ReplyCommand extends BaseCommand {

    private MsgManager msgManager = CrewChat.getInstance().getMsgManager();
    private PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();
    private Chat chat = CrewChat.getChat();

    @Default
    @Description("Replies to last received private message.")
    public void onDefault(CommandSender commandSender, String message) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            playerManager.updateMutedPlayers();

            if (msgManager.playerExists(((Player) commandSender).getName())) {
                if (Bukkit.getPlayer(msgManager.getLastSender(((Player) commandSender).getName())) == null)
                    commandSender.spigot().sendMessage(Messages.playerNoExist());
                else {
                    Player recipient = Bukkit.getPlayer(msgManager.getLastSender(((Player) commandSender).getName()));
                    msgManager.updatePlayer(recipient.getName(), ((Player) commandSender).getName());
                    commandSender.spigot().sendMessage(Messages.privateMessageSend(
                            chat.getPlayerPrefix((Player) commandSender),
                            chat.getPlayerPrefix(recipient), recipient.getName(),
                            playerManager.getStatus((Player) commandSender),
                            playerManager.getStatus(recipient), message));
                    if (!playerManager.hasMuted(recipient, (Player) commandSender))
                        recipient.spigot().sendMessage(Messages.privateMessageReceive(
                                chat.getPlayerPrefix((Player) commandSender),
                                chat.getPlayerPrefix(recipient),
                                ((Player) commandSender).getName(),
                                playerManager.getStatus((Player) commandSender),
                                playerManager.getStatus(recipient), message));
                }
            }
            else commandSender.spigot().sendMessage(Messages.noPMReceived());
        }
    }
}
