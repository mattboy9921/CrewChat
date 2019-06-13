package net.mattlabs.crewchat;

import net.mattlabs.crewchat.util.PlayerManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Party extends Channel {

    private PlayerManager playerManager;
    private ArrayList<Chatter> chatters;

    public Party(String name, ChatColor chatColor) {
        super(name, chatColor, false);
        playerManager = CrewChat.getInstance().getPlayerManager();
        chatters = new ArrayList<>();
    }
}
