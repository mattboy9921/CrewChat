package net.mattlabs.crewchat.listeners;

import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.MsgManager;
import net.mattlabs.crewchat.util.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private MsgManager msgManager;
    private PlayerManager playerManager;

    public QuitListener() {
        msgManager = CrewChat.getInstance().getMsgManager();
        playerManager = CrewChat.getInstance().getPlayerManager();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerManager.updateMutedPlayers();

        Player player = event.getPlayer();
        msgManager.removePlayer(player.getDisplayName());
        if (playerManager.isDeafened(player)) playerManager.setDeafened(player, false);
        if (playerManager.isOnline(player)) playerManager.setOffline(player);
    }
}
