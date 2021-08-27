package net.mattlabs.crewchat;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.scheduler.BukkitTask;

import java.time.LocalDateTime;

public class Party extends Channel {

    private final CrewChat crewChat = CrewChat.getInstance();

    private LocalDateTime time;
    private BukkitTask watchdog;

    public Party(String name) {
        this(name, NamedTextColor.WHITE);
    }

    public Party(String name, TextColor textColor) {
        super(name, "Party", textColor, false, true, false, true);
        time = LocalDateTime.now();
    }

    // Update the time if players are still subscribed to a party
    public void updateTime() {
        crewChat.getLogger().info("Running update time...");
        if (crewChat.getPlayerManager().getOnlineSubscribedPlayers(this.getName()).isEmpty()) {
            if (LocalDateTime.now().isAfter(time.plusMinutes(crewChat.getConfigCC().getPartyTimeout()))) {
                crewChat.getChannelManager().removeChannel(this);
                watchdog.cancel();
            }
        }
        else time = LocalDateTime.now();
    }

    // Check every minute to see if party is empty and past the timeout
    public void initialize() {
        crewChat.getLogger().info("Creating update time...");
        watchdog = crewChat.getServer().getScheduler().runTaskTimerAsynchronously(crewChat, this::updateTime, 1200, 1200);
    }
}
