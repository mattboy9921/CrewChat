package net.mattlabs.crewchat.util;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class PlayerManager {

    private final CrewChat crewChat = CrewChat.getInstance();
    private final ConfigurateManager configurateManager = crewChat.getConfigurateManager();
    private final ChannelManager channelManager = crewChat.getChannelManager();

    private final BukkitAudiences platform = crewChat.getPlatform();

    private final Map<UUID, Chatter> chatters = new HashMap<>();
    private final Map<UUID, Chatter> onlineChatters = new HashMap<>();

    public void loadPlayers() {
        PlayerData playerData = configurateManager.get("playerdata.conf");
        playerData.getChatters().forEach(chatter -> chatters.put(chatter.getUuid(), chatter));
        updateChannels();
        CrewChat.getInstance().getLogger().info(chatters.size() + " player(s) loaded.");
    }

    public void loadOnlinePlayers() {
        for (Player player : CrewChat.getInstance().getServer().getOnlinePlayers()) {
            if (chatters.containsKey(player.getUniqueId()))
                setOnline(player);
        }
        if (CrewChat.getInstance().getServer().getOnlinePlayers().size() == onlineChatters.size())
            CrewChat.getInstance().getLogger().info(onlineChatters.size() + " online player(s) loaded.");
        else
            CrewChat.getInstance().getLogger().warning("Online player mismatch! "
                    + CrewChat.getInstance().getServer().getOnlinePlayers().size() + " SOP, "
                    + onlineChatters.size() + " POP. Configuration error!");
    }

    public void reloadPlayers() {
        chatters.clear();
        onlineChatters.clear();
        loadPlayers();
        loadOnlinePlayers();
        updateChannels();
    }

    public boolean playerExists(Player player) {
        return playerExists((OfflinePlayer) player);
    }

    public boolean playerExists(OfflinePlayer player) {
        if (chatters.isEmpty()) return false;
        return chatters.containsKey(player.getUniqueId());
    }

    public void addPlayer(Player player, String activeChannel, ArrayList<String> subscribedChannels) throws NullPointerException {
        boolean configError = true;
        for (Channel channel : channelManager.getChannels()) {
            if (channel.getName().equals(activeChannel)) {
                configError = false;
                break;
            }
        }
        if (configError) {
            throw new NullPointerException("Bad config/permissions");
        }
        else {
            CrewChat.getInstance().getLogger().info("Player \"" + player.getName() + "\" data doesn't exist, creating...");
            Chatter chatter = new Chatter(player.getUniqueId(), activeChannel, subscribedChannels, new ArrayList<>(), "No status...");

            PlayerData playerData = configurateManager.get("playerdata.conf");
            playerData.addChatter(chatter);

            chatters.put(chatter.getUuid(), chatter);

            configurateManager.save("playerdata.conf");
        }
    }

    public void setOffline(Player player) {
        onlineChatters.remove(player.getUniqueId());
    }

    public void setOnline(Player player) {
        onlineChatters.put(player.getUniqueId(), chatters.get(player.getUniqueId()));
    }

    public boolean isOnline(Player player) {
        if (player.isOnline()) {
            return onlineChatters.containsKey(player.getUniqueId());
        }
        else return false;
    }

    public String getActiveChannel(Player player) {
        return getActiveChannel((OfflinePlayer) player);
    }

    public String getActiveChannel(OfflinePlayer player) {
        return chatters.get(player.getUniqueId()).getActiveChannel();
    }

    public void setActiveChannel(Player player, String channelName) {
        Chatter chatter = chatters.get(player.getUniqueId());
        chatter.setActiveChannel(channelName);
        // Save to config
        PlayerData playerData = configurateManager.get("playerdata.conf");
        playerData.setChatter(chatter);
        configurateManager.save("playerdata.conf");
    }

    public List<String> getSubscribedChannels(Player player) {
        return getSubscribedChannels((OfflinePlayer) player);
    }

    public List<String> getSubscribedChannels(OfflinePlayer player) {
        return chatters.get(player.getUniqueId()).getSubscribedChannels();
    }

    public ArrayList<Player> getSubscribedPlayers(String activeChannel) {
        ArrayList<Player> subscribedPlayers = new ArrayList<>();
        chatters.forEach((uuid, chatter) -> {
            if (chatter.isSubscribedTo(activeChannel)) subscribedPlayers.add(chatter.toPlayer());
        });
        return subscribedPlayers;
    }

    public ArrayList<Player> getOnlineSubscribedPlayers(String activeChannel) {
        ArrayList<Player> subscribedPlayers = new ArrayList<>();
        onlineChatters.values().forEach(chatter -> {
            if (chatter.isOnline() && chatter.isSubscribedTo(activeChannel)) subscribedPlayers.add(chatter.toPlayer());
        });
        return subscribedPlayers;
    }

    public void setStatus(Player player, String status) {
        Chatter chatter = chatters.get(player.getUniqueId());
        chatter.setStatus(status);
        // Save to config
        PlayerData playerData = configurateManager.get("playerdata.conf");
        playerData.setChatter(chatter);
        configurateManager.save("playerdata.conf");
    }

    public String getStatus(Player player) {
        return getStatus((OfflinePlayer) player);
    }

    public String getStatus(OfflinePlayer player) {
        return chatters.get(player.getUniqueId()).getStatus();
    }

    public ArrayList<Mutee> getMutedPlayers(Player player) {
        return getMutedPlayers((OfflinePlayer) player);
    }

    public ArrayList<Mutee> getMutedPlayers(OfflinePlayer player) {
        return chatters.get(player.getUniqueId()).getMutedPlayers();
    }

    public ArrayList<String> getMutedPlayerNames(Player player) {
        return getMutedPlayerNames((OfflinePlayer) player);
    }

    public ArrayList<String> getMutedPlayerNames(OfflinePlayer player) {
        ArrayList<String> mutedPlayerNames = new ArrayList<>();
        chatters.get(player.getUniqueId()).getMutedPlayers().forEach(mutee -> mutedPlayerNames.add(mutee.getName()));
        return mutedPlayerNames;
    }

    public void addSubscription(Player player, String channelName) {
        Chatter chatter = chatters.get(player.getUniqueId());
        chatter.addSubscription(channelName);
        // Save to config
        PlayerData playerData = configurateManager.get("playerdata.conf");
        playerData.setChatter(chatter);
        configurateManager.save("playerdata.conf");
    }

    public void removeSubscription(Player player, String channelName) {
        Chatter chatter = chatters.get(player.getUniqueId());
        chatter.removeSubscription(channelName);
        // Save to config
        PlayerData playerData = configurateManager.get("playerdata.conf");
        playerData.setChatter(chatter);
        configurateManager.save("playerdata.conf");
    }

    public void addMutedPlayer(Player muterPlayer, Player muteePlayer) {
        Chatter muter = chatters.get(muterPlayer.getUniqueId());
        muter.addMutedPlayer(muteePlayer.getUniqueId());
        configurateManager.save("playerdata.conf");
    }

    public void removeMutedPlayer(Player muterPlayer, Player muteePlayer) {
        Chatter muter = chatters.get(muterPlayer.getUniqueId());
        muter.removeMutedPlayer(muteePlayer.getUniqueId());
        configurateManager.save("playerdata.conf");
    }

    public void removeMutedPlayer(Player muterPlayer, OfflinePlayer muteePlayer) {
        Chatter muter = chatters.get(muterPlayer.getUniqueId());
        muter.removeMutedPlayer(muteePlayer.getUniqueId());
        configurateManager.save("playerdata.conf");
    }

    public void updateMutedPlayers() {
        CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), () -> {
            for (Chatter chatter : onlineChatters.values()) {
                ArrayList<Mutee> removedMutees = new ArrayList<>();
                for (Mutee mutee : chatter.getMutedPlayers())
                    if (LocalDateTime.now().isAfter(mutee.getTime().plusHours(24))) {
                        removedMutees.add(mutee);
                        if (Bukkit.getOfflinePlayer(chatter.getUuid()).isOnline())
                            platform.player(Objects.requireNonNull(Bukkit.getPlayer(chatter.getUuid()))).sendMessage(crewChat.getMessages().playerUnmuted(mutee.getPrefix(), mutee.getName()));
                    }
                for (Mutee mutee : removedMutees) chatter.removeMutedPlayer(mutee.getUuid());
            }
        });
    }

    public void updateChannels(Player player) {
        Chatter chatter = chatters.get(player.getUniqueId());
        chatter.getSubscribedChannels().removeIf(s -> !channelManager.getChannelNames().contains(s));
        if (!channelManager.getChannelNames().contains(chatter.getActiveChannel())) chatter.setActiveChannel(chatter.getSubscribedChannels().get(0));
        crewChat.getServer().getScheduler().runTaskAsynchronously(crewChat, () -> configurateManager.save("playerdata.conf"));
    }

    private void updateChannels() {
        chatters.values().forEach(chatter -> {
            chatter.getSubscribedChannels().removeIf(s -> !channelManager.getChannelNames().contains(s));
            if (!channelManager.getChannelNames().contains(chatter.getActiveChannel())) chatter.setActiveChannel(chatter.getSubscribedChannels().get(0));
        });
        crewChat.getServer().getScheduler().runTaskAsynchronously(crewChat, () -> configurateManager.save("playerdata.conf"));
    }

    public boolean hasMuted(Player muter, Player mutee) {
        return onlineChatters.get(muter.getUniqueId()).hasMuted(mutee.getUniqueId());
    }

    public boolean isDeafened(Player player) {
        return onlineChatters.get(player.getUniqueId()).isDeafened();
    }

    public void setDeafened(Player player, boolean deafen) {
        onlineChatters.get(player.getUniqueId()).setDeafened(deafen);
    }

    public int getPlayerCount() {
        return chatters.size();
    }

    public int getOnlinePlayerCount() {
        return onlineChatters.size();
    }

    public ArrayList<String> getPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        chatters.values().forEach(chatter -> playerNames.add(Bukkit.getOfflinePlayer(chatter.getUuid()).getName()));
        return playerNames;
    }
}
