package net.mattlabs.crewchat.util;

import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.Chatter;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.PlayerData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    private ConfigurateManager configurateManager;
    private ChannelManager channelManager;
    private ArrayList<Chatter> chatters, onlineChatters;

    public PlayerManager() {
        configurateManager = CrewChat.getInstance().getConfigurateManager();
        channelManager = CrewChat.getInstance().getChannelManager();
        chatters = new ArrayList<>();
        onlineChatters = new ArrayList<>();
    }

    public void loadPlayers() {
        PlayerData playerData = configurateManager.get("playerdata.conf");
        chatters = playerData.getChatters();
        CrewChat.getInstance().getLogger().info(chatters.size() + " player(s) loaded.");
    }

    public void loadOnlinePlayers() {
        for (Player player : CrewChat.getInstance().getServer().getOnlinePlayers()) {
            if (chatters.contains(new Chatter(player.getUniqueId(), null, null, null)))
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
            if (channel.getName().equals(activeChannel)) configError = false;
        }
        if (configError) {
            throw new NullPointerException("Bad config/permissions");
        }
        else {
            CrewChat.getInstance().getLogger().info("Player \"" + player.getName() + "\" data doesn't exist, creating...");
            Chatter chatter = new Chatter(player.getUniqueId(), activeChannel, subscribedChannels, "No status...");

            PlayerData playerData = configurateManager.get("playerdata.conf");
            playerData.addChatter(chatter);

            chatters.add(chatter);

            configurateManager.save("playerdata.conf");
        }
    }

    public void setOffline(Player player) {
        Chatter chatter = chatters.get(chatters.indexOf(new Chatter(player.getUniqueId(), null, null, null)));
        onlineChatters.remove(chatter);
    }

    public void setOnline(Player player) {
        Chatter chatter = chatters.get(chatters.indexOf(new Chatter(player.getUniqueId(), null, null, null)));
        onlineChatters.add(chatter);
    }

    public boolean isOnline(Player player) {
        if (player.isOnline()) {
            return onlineChatters.contains(new Chatter(player.getUniqueId(), null, null, null));
        }
        else return false;
    }

    public String getActiveChannel(Player player) {
        return chatters.get(chatters.lastIndexOf(new Chatter(player.getUniqueId(), null, null, null))).getActiveChannel();
    }

    public void setActiveChannel(Player player, String channelName) {
        Chatter chatter = new Chatter(player.getUniqueId(), null, null, null);
        chatter = chatters.get(chatters.indexOf(chatter));
        chatter.setActiveChannel(channelName);
        // Save to config
        PlayerData playerData = configurateManager.get("playerdata.conf");
        playerData.setChatter(chatter);
        configurateManager.save("playerdata.conf");
    }

    public List<String> getSubscribedChannels(Player player) {
        return chatters.get(chatters.lastIndexOf(new Chatter(player.getUniqueId(), null, null, null))).getSubscribedChannels();
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
        Chatter chatter = new Chatter(player.getUniqueId(), null, null, null);
        chatter = chatters.get(chatters.indexOf(chatter));
        chatter.setStatus(status);
        // Save to config
        PlayerData playerData = configurateManager.get("playerdata.conf");
        playerData.setChatter(chatter);
        configurateManager.save("playerdata.conf");
    }

    public String getStatus(Player player) {
        return chatters.get(chatters.lastIndexOf(new Chatter(player.getUniqueId(), null, null, null))).getStatus();
    }

    public void addSubscription(Player player, String channelName) {
        Chatter chatter = new Chatter(player.getUniqueId(), null, null, null);
        chatter = chatters.get(chatters.indexOf(chatter));
        chatter.addSubscription(channelName);
        // Save to config
        PlayerData playerData = configurateManager.get("playerdata.conf");
        playerData.setChatter(chatter);
        configurateManager.save("playerdata.conf");
    }

    public void removeSubscription(Player player, String channelName) {
        Chatter chatter = new Chatter(player.getUniqueId(), null, null, null);
        chatter = chatters.get(chatters.indexOf(chatter));
        chatter.removeSubscription(channelName);
        // Save to config
        PlayerData playerData = configurateManager.get("playerdata.conf");
        playerData.setChatter(chatter);
        configurateManager.save("playerdata.conf");
    }
}
