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

    private ArrayList<Chatter> chatters = new ArrayList<>();
    private final ArrayList<Chatter> onlineChatters = new ArrayList<>();

    public void loadPlayers() {
        PlayerData playerData = configurateManager.get("playerdata.conf");
        chatters = playerData.getChatters();
        updateChannels();
        CrewChat.getInstance().getLogger().info(chatters.size() + " player(s) loaded.");
    }

    public void loadOnlinePlayers() {
        for (Player player : CrewChat.getInstance().getServer().getOnlinePlayers()) {
            if (chatters.contains(new Chatter(player.getUniqueId())))
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
        if (chatters == null) return false;
        for (Chatter chatter : chatters) {
            if (chatter.getUuid().equals(player.getUniqueId())) return true;
        }
        return false;
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

            chatters.add(chatter);

            configurateManager.save("playerdata.conf");
        }
    }

    public void setOffline(Player player) {
        Chatter chatter = chatters.get(chatters.indexOf(new Chatter(player.getUniqueId())));
        onlineChatters.remove(chatter);
    }

    public void setOnline(Player player) {
        Chatter chatter = chatters.get(chatters.indexOf(new Chatter(player.getUniqueId())));
        onlineChatters.add(chatter);
    }

    public boolean isOnline(Player player) {
        if (player.isOnline()) {
            return onlineChatters.contains(new Chatter(player.getUniqueId()));
        }
        else return false;
    }

    public String getActiveChannel(Player player) {
        return getActiveChannel((OfflinePlayer) player);
    }

    public String getActiveChannel(OfflinePlayer player) {
        return chatters.get(chatters.lastIndexOf(new Chatter(player.getUniqueId()))).getActiveChannel();
    }

    public void setActiveChannel(Player player, String channelName) {
        Chatter chatter = new Chatter(player.getUniqueId());
        chatter = chatters.get(chatters.indexOf(chatter));
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
        return chatters.get(chatters.lastIndexOf(new Chatter(player.getUniqueId()))).getSubscribedChannels();
    }

    public ArrayList<Player> getSubscribedPlayers(String activeChannel) {
        ArrayList<Player> subscribedPlayers = new ArrayList<>();
        for (Chatter chatter : chatters) {
            if (chatter.isSubscribedTo(activeChannel)) {
                subscribedPlayers.add(chatter.toPlayer());
            }
        }
        return subscribedPlayers;
    }

    public ArrayList<Player> getOnlineSubscribedPlayers(String activeChannel) {
        ArrayList<Player> subscribedPlayers = new ArrayList<>();
        for (Chatter chatter : onlineChatters) {
            if (chatter.isOnline() && chatter.isSubscribedTo(activeChannel)) {
                subscribedPlayers.add(chatter.toPlayer());
            }
        }
        return subscribedPlayers;
    }

    public void setStatus(Player player, String status) {
        Chatter chatter = new Chatter(player.getUniqueId());
        chatter = chatters.get(chatters.indexOf(chatter));
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
        return chatters.get(chatters.lastIndexOf(new Chatter(player.getUniqueId()))).getStatus();
    }

    public ArrayList<Mutee> getMutedPlayers(Player player) {
        return getMutedPlayers((OfflinePlayer) player);
    }

    public ArrayList<Mutee> getMutedPlayers(OfflinePlayer player) {
        return chatters.get(chatters.indexOf(new Chatter(player.getUniqueId()))).getMutedPlayers();
    }

    public ArrayList<String> getMutedPlayerNames(Player player) {
        return getMutedPlayerNames((OfflinePlayer) player);
    }

    public ArrayList<String> getMutedPlayerNames(OfflinePlayer player) {
        ArrayList<String> mutedPlayerNames = new ArrayList<>();
        chatters.get(chatters.lastIndexOf(new Chatter(player.getUniqueId()))).getMutedPlayers().forEach(mutee ->
                mutedPlayerNames.add(mutee.getName()));
        return mutedPlayerNames;
    }

    public void addSubscription(Player player, String channelName) {
        Chatter chatter = new Chatter(player.getUniqueId());
        chatter = chatters.get(chatters.indexOf(chatter));
        chatter.addSubscription(channelName);
        // Save to config
        PlayerData playerData = configurateManager.get("playerdata.conf");
        playerData.setChatter(chatter);
        configurateManager.save("playerdata.conf");
    }

    public void removeSubscription(Player player, String channelName) {
        Chatter chatter = new Chatter(player.getUniqueId());
        chatter = chatters.get(chatters.indexOf(chatter));
        chatter.removeSubscription(channelName);
        // Save to config
        PlayerData playerData = configurateManager.get("playerdata.conf");
        playerData.setChatter(chatter);
        configurateManager.save("playerdata.conf");
    }

    public void addMutedPlayer(Player muterPlayer, Player muteePlayer) {
        Chatter muter = new Chatter(muterPlayer.getUniqueId());
        muter = chatters.get(chatters.indexOf(muter));
        muter.addMutedPlayer(muteePlayer.getUniqueId());
        configurateManager.save("playerdata.conf");
    }

    public void removeMutedPlayer(Player muterPlayer, Player muteePlayer) {
        Chatter muter = new Chatter(muterPlayer.getUniqueId());
        muter = chatters.get(chatters.indexOf(muter));
        muter.removeMutedPlayer(muteePlayer.getUniqueId());
        configurateManager.save("playerdata.conf");
    }

    public void removeMutedPlayer(Player muterPlayer, OfflinePlayer muteePlayer) {
        Chatter muter = new Chatter(muterPlayer.getUniqueId());
        muter = chatters.get(chatters.indexOf(muter));
        muter.removeMutedPlayer(muteePlayer.getUniqueId());
        configurateManager.save("playerdata.conf");
    }

    public void updateMutedPlayers() {
        CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), () -> {
            for (Chatter chatter : onlineChatters) {
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
        Chatter chatter = chatters.get(chatters.lastIndexOf(new Chatter(player.getUniqueId())));
        chatter.getSubscribedChannels().removeIf(s -> !channelManager.getChannelNames().contains(s));
        if (!channelManager.getChannelNames().contains(chatter.getActiveChannel())) chatter.setActiveChannel(chatter.getSubscribedChannels().get(0));
        crewChat.getServer().getScheduler().runTaskAsynchronously(crewChat, () -> configurateManager.save("playerdata.conf"));
    }

    private void updateChannels() {
        for (Chatter chatter : chatters) {
            chatter.getSubscribedChannels().removeIf(s -> !channelManager.getChannelNames().contains(s));
            if (!channelManager.getChannelNames().contains(chatter.getActiveChannel())) chatter.setActiveChannel(chatter.getSubscribedChannels().get(0));
        }
        crewChat.getServer().getScheduler().runTaskAsynchronously(crewChat, () -> configurateManager.save("playerdata.conf"));
    }

    public boolean hasMuted(Player muter, Player mutee) {
        return onlineChatters.get(onlineChatters.lastIndexOf(new Chatter(muter.getUniqueId()))).hasMuted(mutee.getUniqueId());
    }

    public boolean isDeafened(Player player) {
        return onlineChatters.get(onlineChatters.lastIndexOf(new Chatter(player.getUniqueId()))).isDeafened();
    }

    public void setDeafened(Player player, boolean deafen) {
        onlineChatters.get(onlineChatters.lastIndexOf(new Chatter(player.getUniqueId()))).setDeafened(deafen);
    }

    public int getPlayerCount() {
        return chatters.size();
    }

    public int getOnlinePlayerCount() {
        return onlineChatters.size();
    }

    public ArrayList<String> getPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        chatters.forEach(chatter -> playerNames.add(Bukkit.getOfflinePlayer(chatter.getUuid()).getName()));
        return playerNames;
    }
}
