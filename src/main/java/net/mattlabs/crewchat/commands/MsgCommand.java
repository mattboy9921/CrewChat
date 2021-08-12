package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.MsgManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

@CommandAlias("msg|pm|tell|whisper|w")
@CommandPermission("crewchat.pm")
public class MsgCommand extends BaseCommand {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final MsgManager msgManager = crewChat.getMsgManager();
    private final PlayerManager playerManager = crewChat.getPlayerManager();
    private final BukkitAudiences platform = crewChat.getPlatform();
    private final Chat chat = CrewChat.getChat();

    @Default
    @CommandCompletion("@players @nothing")
    @Description("Sends a private message to another player.")
    public void onDefault(CommandSender commandSender, String recipientString, String message) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            playerManager.updateMutedPlayers();

            Player sender = (Player) commandSender; 
            Player recipient = Bukkit.getPlayerExact(recipientString);
            if (recipient == null) platform.player(sender).sendMessage(crewChat.getMessages().playerNoExist());
            else if (sender.getName().equalsIgnoreCase(recipient.getName())) {
                platform.player(sender).sendMessage(crewChat.getMessages().cantMessageSelf());
            }
            else {
                SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, HH:mm:ss");
                String time = format.format(new Date());

                msgManager.updatePlayer(recipient.getName(), sender.getName());
                platform.player(sender).sendMessage(crewChat.getMessages().privateMessageSend(chat.getPlayerPrefix(sender),
                        chat.getPlayerPrefix(recipient), recipient.getName(),
                        playerManager.getStatus(sender),
                        playerManager.getStatus(recipient), time, message));
                if (!playerManager.hasMuted(recipient, sender))
                    platform.player(recipient).sendMessage(crewChat.getMessages().privateMessageReceive(chat.getPlayerPrefix(sender),
                            chat.getPlayerPrefix(recipient), sender.getName(),
                            playerManager.getStatus(sender),
                            playerManager.getStatus(recipient), time, message));
            }
        }
    }
}
