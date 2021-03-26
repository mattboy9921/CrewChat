package net.mattlabs.crewchat.util;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.mattlabs.crewchat.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    }

    public boolean playerExists(Player player) {
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
        return chatters.get(chatters.lastIndexOf(new Chatter(player.getUniqueId()))).getSubscribedChannels();
    }

    public ArrayList<Player> getSubscribedPlayers(String activeChannel) {
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
        return chatters.get(chatters.lastIndexOf(new Chatter(player.getUniqueId()))).getStatus();
    }

    public ArrayList<String> getMutedPlayerNames(Player player) {
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

    public void updateMutedPlayers() {
        CrewChat.getInstance().getServer().getScheduler().runTaskAsynchronously(CrewChat.getInstance(), () -> {
            for (Chatter chatter : onlineChatters)
                for (Mutee mutee : chatter.getMutedPlayers())
                    if (mutee.getTime().isAfter(mutee.getTime().plusHours(24))) {
                        chatter.removeMutedPlayer(mutee.getUuid());
                        if (Bukkit.getOfflinePlayer(chatter.getUuid()).isOnline())
                            platform.player(Objects.requireNonNull(Bukkit.getPlayer(chatter.getUuid()))).sendMessage(crewChat.getMessages().playerUnmuted(mutee.getPrefix(), mutee.getName()));
                    }
        });
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
}
