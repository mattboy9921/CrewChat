package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.MsgManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

@CommandAlias("reply|r")
@CommandPermission("crewchat.pm")
public class ReplyCommand extends BaseCommand {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final MsgManager msgManager = crewChat.getMsgManager();
    private final PlayerManager playerManager = crewChat.getPlayerManager();
    private final BukkitAudiences platform = crewChat.getPlatform();
    private final Chat chat = CrewChat.getChat();

    @Default
    @Description("Replies to last received private message.")
    @CommandCompletion("@nothing")
    @SuppressWarnings("ConstantConditions")
    public void onDefault(Player sender, String message) {
        playerManager.updateMutedPlayers();

        // Check if player has received a message
        if (msgManager.playerExists(sender.getName())) {
            // Check if recipient still online
            if (Bukkit.getPlayer(msgManager.getLastSender(sender.getName())) == null)
                platform.player(sender).sendMessage(crewChat.getMessages().privateMessage().playerNoExist());
            else {
                SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, HH:mm:ss");
                String time = format.format(new Date());


                Player recipient = Bukkit.getPlayer(msgManager.getLastSender(sender.getName()));
                msgManager.updatePlayer(recipient.getName(), sender.getName()); // Never null, check for online
                // Send message to sender
                platform.player(sender).sendMessage(crewChat.getMessages().privateMessage().privateMessageSend(chat.getPlayerPrefix(sender),
                        chat.getPlayerPrefix(recipient), recipient.getName(),
                        playerManager.getStatus(sender),
                        playerManager.getStatus(recipient), time, message));

                // If not muted, send to recipient
                if (!playerManager.hasMuted(recipient, sender))
                    platform.player(recipient).sendMessage(crewChat.getMessages().privateMessage().privateMessageReceive(chat.getPlayerPrefix(sender),
                            chat.getPlayerPrefix(recipient), sender.getName(),
                            playerManager.getStatus(sender),
                            playerManager.getStatus(recipient), time, message));
            }
        }
        else platform.player(sender).sendMessage(crewChat.getMessages().privateMessage().noPMReceived());
    }
}
