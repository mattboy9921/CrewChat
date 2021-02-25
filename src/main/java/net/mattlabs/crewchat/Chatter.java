package net.mattlabs.crewchat;

import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.UUID;

@ConfigSerializable
public class Chatter {

    private transient UUID uuid;
    private String activeChannel, status;
    private ArrayList<String> subscribedChannels;
    private ArrayList<UUID> mutedPlayers;

    // Empty constructor for Configurate
    public Chatter() {}

    public Chatter(UUID uuid) {
        this.uuid = uuid;
        activeChannel = null;
        subscribedChannels = null;
        mutedPlayers = null;
        status = null;
    }

    public Chatter(UUID uuid, String activeChannel, ArrayList<String> subscribedChannels, ArrayList<UUID> mutedPlayers, String status) {
        this.uuid = uuid;
        this.activeChannel = activeChannel;
        this.subscribedChannels = subscribedChannels;
        this.mutedPlayers = mutedPlayers;
        this.status = status;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player toPlayer() {
        for (Player player : CrewChat.getInstance().getServer().getOnlinePlayers()) {
            if (player.getUniqueId().equals(uuid)) return player;
        }
        return null;
    }

    public boolean isSubscribedTo(String activeChannel) {
        for (String channel : subscribedChannels) {
            if (channel.equalsIgnoreCase(activeChannel)) return true;
        }
        return false;
    }

    public boolean isOnline() {
        for (Player player : CrewChat.getInstance().getServer().getOnlinePlayers()) {
            if (player.getUniqueId().equals(uuid)) return true;
        }
        return false;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getActiveChannel() {
        return activeChannel;
    }

    public ArrayList<String> getSubscribedChannels() {
        return subscribedChannels;
    }

    public void addSubscription(String channelName) {
        subscribedChannels.add(channelName);
    }

    public void removeSubscription(String channelName) {
        subscribedChannels.remove(channelName);
    }

    public void setActiveChannel(String activeChannel) {
        this.activeChannel = activeChannel;
    }

    public ArrayList<UUID> getMutedPlayers() {
        return mutedPlayers;
    }

    public void addMutedPlayer(UUID uuid) {
        mutedPlayers.add(uuid);
    }

    public void removeMutedPlayer(UUID uuid) {
        mutedPlayers.remove(uuid);
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof Chatter) return uuid.equals(((Chatter) object).uuid);
        else return false;
    }
}
