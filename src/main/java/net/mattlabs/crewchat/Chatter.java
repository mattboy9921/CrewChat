package net.mattlabs.crewchat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.UUID;

@ConfigSerializable
public class Chatter {

    private transient UUID uuid;
    private String activeChannel, status;
    private ArrayList<String> subscribedChannels;
    private ArrayList<Mutee> mutedPlayers;
    private transient boolean deafened;

    // Empty constructor for Configurate
    public Chatter() {}

    public Chatter(UUID uuid) {
        this.uuid = uuid;
        activeChannel = null;
        subscribedChannels = null;
        mutedPlayers = null;
        status = null;
        deafened = false;
    }

    public Chatter(UUID uuid, String activeChannel, ArrayList<String> subscribedChannels, ArrayList<Mutee> mutedPlayers, String status) {
        this.uuid = uuid;
        this.activeChannel = activeChannel;
        this.subscribedChannels = subscribedChannels;
        this.mutedPlayers = mutedPlayers;
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
        return mutedPlayers;
    }

    public void addMutedPlayer(UUID uuid) {
        if (mutedPlayers.contains(new Mutee(uuid, null, null)))
            mutedPlayers.get(mutedPlayers.lastIndexOf(new Mutee(uuid, null, null))).updateTime();
        else {
            mutedPlayers.add(new Mutee(uuid, CrewChat.getChat().getPlayerPrefix(Bukkit.getPlayer(uuid)), Bukkit.getPlayer(uuid).getName()));
            CrewChat.getInstance().getLogger().info(mutedPlayers.get(mutedPlayers.lastIndexOf(new Mutee(uuid,null, null))).getTime().toString());
        }
    }

    public void removeMutedPlayer(UUID uuid) {
        mutedPlayers.remove(new Mutee(uuid, null, null));
    }

    public boolean hasMuted(UUID uuid) {
        return mutedPlayers.contains(new Mutee(uuid, null, null));
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
