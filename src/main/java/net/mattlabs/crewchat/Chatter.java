package net.mattlabs.crewchat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.*;

@ConfigSerializable
public class Chatter {

    private transient UUID uuid;
    private String activeChannel, status;
    private ArrayList<String> subscribedChannels;
    private Map<UUID, Mutee> mutedPlayers;
    private transient boolean deafened;

    // Empty constructor for Configurate
    @SuppressWarnings("unused")
    public Chatter() {}

    public Chatter(UUID uuid, String activeChannel, ArrayList<String> subscribedChannels, ArrayList<Mutee> mutedPlayers, String status) {
        this.uuid = uuid;
        this.activeChannel = activeChannel;
        this.subscribedChannels = subscribedChannels;
        this.mutedPlayers = new HashMap<>();
        mutedPlayers.forEach(mutee -> this.mutedPlayers.put(mutee.getUuid(), mutee));
        this.status = status;
        deafened = false;
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

    public ArrayList<Mutee> getMutedPlayers() {
        return new ArrayList<>(mutedPlayers.values());
    }

    public void addMutedPlayer(UUID uuid) {
        if (mutedPlayers.containsKey(uuid)) mutedPlayers.get(uuid).updateTime();
        else mutedPlayers.put(uuid, new Mutee(uuid, CrewChat.getChat().getPlayerPrefix(Bukkit.getPlayer(uuid)), Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName()));
    }

    public void removeMutedPlayer(UUID uuid) {
        mutedPlayers.remove(uuid);
    }

    public boolean hasMuted(UUID uuid) {
        return mutedPlayers.containsKey(uuid);
    }

    public boolean isDeafened() {
        return deafened;
    }

    public void setDeafened(boolean deafened) {
        this.deafened = deafened;
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof Chatter) return uuid.equals(((Chatter) object).uuid);
        else return false;
    }
}
