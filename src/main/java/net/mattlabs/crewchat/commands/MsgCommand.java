package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.MsgManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("msg|pm|tell|whisper|w")
@CommandPermission("crewchat.pm")
public class MsgCommand extends BaseCommand {

    private final MsgManager msgManager = CrewChat.getInstance().getMsgManager();
    private final PlayerManager playerManager = CrewChat.getInstance().getPlayerManager();
    private final BukkitAudiences platform = CrewChat.getInstance().getPlatform();
    private final Chat chat = CrewChat.getChat();
    private final Messages messages = CrewChat.getInstance().getMessages();

    @Default
    @CommandCompletion("@players @nothing")
    @Description("Sends a private message to another player.")
    public void onDefault(CommandSender commandSender, String recipientString, String message) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            playerManager.updateMutedPlayers();

            Player recipient = Bukkit.getPlayerExact(recipientString);
            if (recipient == null) platform.player((Player) commandSender).sendMessage(messages.playerNoExist());
            else if (commandSender.getName().equalsIgnoreCase(recipient.getName())) {
                platform.player((Player) commandSender).sendMessage(messages.cantMessageSelf());
            }
            else {
                msgManager.updatePlayer(recipient.getName(), commandSender.getName());
                platform.player((Player) commandSender).sendMessage(messages.privateMessageSend(chat.getPlayerPrefix((Player) commandSender),
                        chat.getPlayerPrefix(recipient), recipient.getName(),
                        playerManager.getStatus((Player) commandSender),
                        playerManager.getStatus(recipient), message));
                if (!playerManager.hasMuted(recipient, (Player) commandSender))
                    platform.player((Player) commandSender).sendMessage(messages.privateMessageReceive(chat.getPlayerPrefix((Player) commandSender),
                            chat.getPlayerPrefix(recipient), commandSender.getName(),
                            playerManager.getStatus((Player) commandSender),
                            playerManager.getStatus(recipient), message));
            }
        }
    }
}
