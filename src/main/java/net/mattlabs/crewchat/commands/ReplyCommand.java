package net.mattlabs.crewchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.MsgManager;
import net.mattlabs.crewchat.util.PlayerManager;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
    public void onDefault(CommandSender commandSender, String message) {
        if (!(commandSender instanceof Player)) CrewChat.getInstance().getLogger().info("Can't be run from console!");
        else {
            playerManager.updateMutedPlayers();

            if (msgManager.playerExists(commandSender.getName())) {
                if (Bukkit.getPlayer(msgManager.getLastSender(commandSender.getName())) == null)
                    platform.player((Player) commandSender).sendMessage(crewChat.getMessages().playerNoExist());
                else {
                    Player recipient = Bukkit.getPlayer(msgManager.getLastSender(commandSender.getName()));
                    msgManager.updatePlayer(recipient.getName(), commandSender.getName());
                    platform.player((Player) commandSender).sendMessage(crewChat.getMessages().privateMessageSend(chat.getPlayerPrefix((Player) commandSender),
                            chat.getPlayerPrefix(recipient), recipient.getName(),
                            playerManager.getStatus((Player) commandSender),
                            playerManager.getStatus(recipient), message));

                    if (!playerManager.hasMuted(recipient, (Player) commandSender))
                        platform.player((Player) commandSender).sendMessage(crewChat.getMessages().privateMessageReceive(chat.getPlayerPrefix((Player) commandSender),
                                chat.getPlayerPrefix(recipient), commandSender.getName(),
                                playerManager.getStatus((Player) commandSender),
                                playerManager.getStatus(recipient), message));
                }
            }
            else platform.player((Player) commandSender).sendMessage(crewChat.getMessages().noPMReceived());
        }
    }
}
