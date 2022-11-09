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

@CommandAlias("msg|pm|tell|whisper|w")
@CommandPermission("crewchat.pm")
public class MsgCommand extends BaseCommand {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final MsgManager msgManager = crewChat.getMsgManager();
    private final PlayerManager playerManager = crewChat.getPlayerManager();
    private final BukkitAudiences platform = crewChat.getPlatform();
    private final Chat chat = CrewChat.getChat();

    @Default
    @Description("Sends a private message to another player.")
    @CommandCompletion("@players @nothing")
    public void onDefault(Player sender, @Name("recipient") String recipientString, String message) {
        playerManager.updateMutedPlayers();

        Player recipient = Bukkit.getPlayerExact(recipientString);
        // Check if player exists
        if (recipient == null) platform.player(sender).sendMessage(crewChat.getMessages().privateMessage().playerNoExist());
        // Check if messaging self
        else if (sender.getName().equalsIgnoreCase(recipient.getName())) {
            platform.player(sender).sendMessage(crewChat.getMessages().privateMessage().cantMessageSelf());
        }
        else {
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, HH:mm:ss");
            String time = format.format(new Date());

            msgManager.updatePlayer(recipient.getName(), sender.getName());
            // Send message to sender
            platform.player(sender).sendMessage(crewChat.getMessages().privateMessage().privateMessageSend(chat.getPlayerPrefix(sender),
                    chat.getPlayerPrefix(recipient), recipient.getName(),
                    playerManager.getStatus(sender),
                    playerManager.getStatus(recipient), time, message));
            // Check if recipient is muted, otherwise send message
            if (!playerManager.hasMuted(recipient, sender))
                platform.player(recipient).sendMessage(crewChat.getMessages().privateMessage().privateMessageReceive(chat.getPlayerPrefix(sender),
                        chat.getPlayerPrefix(recipient), sender.getName(),
                        playerManager.getStatus(sender),
                        playerManager.getStatus(recipient), time, message));
        }
    }
}
