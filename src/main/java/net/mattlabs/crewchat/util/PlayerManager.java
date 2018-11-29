package net.mattlabs.crewchat.util;

import net.mattlabs.crewchat.Chatter;
import net.mattlabs.crewchat.CrewChat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private ConfigManager configManager;
    private ArrayList<Chatter> chatters, onlineChatters;

    public PlayerManager() {
        configManager = CrewChat.getInstance().getConfigManager();
        chatters = new ArrayList<>();
        onlineChatters = new ArrayList<>();
    }

    public void loadPlayers() {
        ConfigurationSection playersConfig = configManager.getFileConfig("playerdata.yml")
                .getConfigurationSection("players");
        for (String key : playersConfig.getKeys(false)) {
            String activeChannel = playersConfig.getConfigurationSection(key).getString("active-channel");
            ArrayList<String> subscribedChannels = new ArrayList<>(playersConfig.getConfigurationSection(key).getStringList("subscribed-channels"));
            String status = playersConfig.getConfigurationSection(key).getString("status");
            chatters.add(new Chatter(UUID.fromString(key), activeChannel, subscribedChannels, status));
        }
    }

    public void reloadPlayers() {
        chatters.clear();
        loadPlayers();
    }

    public boolean playerExists(Player player) {
        if (chatters == null) return false;
        for (Chatter chatter : chatters) {
            if (chatter.getUuid().equals(player.getUniqueId())) return true;
        }
        return false;
    }

    public void addPlayer(Player player, String activeChannel, ArrayList<String> subscribedChannels) {
        if (activeChannel == null) CrewChat.getInstance().getLogger().warning(
                "Player " + player.getName() + " could not be added, check permissions!");
        else {
            CrewChat.getInstance().getLogger().info("Player \"" + player.getName() + "\" data doesn't exist, creating...");
            String status = "No status...";

            ConfigurationSection playersConfig = configManager.getFileConfig("playerdata.yml")
                    .getConfigurationSection("players");
            ConfigurationSection playerConfig = playersConfig.createSection(player.getUniqueId().toString());

            playerConfig.createSection("active-channel");
            playerConfig.set("active-channel", activeChannel);

            playerConfig.createSection("subscribed-channels");
            playerConfig.set("subscribed-channels", subscribedChannels);

            playerConfig.createSection("status");
            playerConfig.set("status", status);

            chatters.add(new Chatter(player.getUniqueId(), activeChannel, subscribedChannels, status));

            configManager.saveConfig("playerdata.yml");
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
            Chatter chatter = chatters.get(chatters.indexOf(new Chatter(player.getUniqueId(), null, null, null)));
            return onlineChatters.contains(chatter);
        }
        else return false;
    }

    public String getActiveChannel(Player player) {
        return configManager.getFileConfig("playerdata.yml")
                .getConfigurationSection("players." + player.getUniqueId().toString()).getString("active-channel");
    }

    public void setActiveChannel(Player player, String channelName) {
        Chatter chatter = new Chatter(player.getUniqueId(), null, null, null);
        chatter = chatters.get(chatters.indexOf(chatter));
        chatter.setActiveChannel(channelName);
        // Save to config
        ConfigurationSection playersConfig = configManager.getFileConfig("playerdata.yml")
                .getConfigurationSection("players." + player.getUniqueId().toString());
        playersConfig.set("active-channel", channelName);
        configManager.saveConfig("playerdata.yml");
    }

    public List<String> getSubscribedChannels(Player player) {
        return configManager.getFileConfig("playerdata.yml")
                .getConfigurationSection("players." + player.getUniqueId().toString()).getStringList("subscribed-channels");
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
        ConfigurationSection playersConfig = configManager.getFileConfig("playerdata.yml")
                .getConfigurationSection("players." + player.getUniqueId().toString());
        playersConfig.set("status", status);
        configManager.saveConfig("playerdata.yml");
    }

    public String getStatus(Player player) {
        Chatter chatter = new Chatter(player.getUniqueId(), null, null, null);
        chatter = chatters.get(chatters.indexOf(chatter));
        return chatter.getStatus();
    }

    public void addSubscription(Player player, String channelName) {
        Chatter chatter = new Chatter(player.getUniqueId(), null, null, null);
        chatter = chatters.get(chatters.indexOf(chatter));
        chatter.addSubscription(channelName);
        // Save to config
        ConfigurationSection playersConfig = configManager.getFileConfig("playerdata.yml")
                .getConfigurationSection("players." + player.getUniqueId().toString());
        List<String> subs = playersConfig.getStringList("subscribed-channels");
        subs.add(channelName);
        playersConfig.set("subscribed-channels", subs);
        configManager.saveConfig("playerdata.yml");
    }

    public void removeSubscription(Player player, String channelName) {
        Chatter chatter = new Chatter(player.getUniqueId(), null, null, null);
        chatter = chatters.get(chatters.indexOf(chatter));
        chatter.removeSubscription(channelName);
        // Save to config
        ConfigurationSection playersConfig = configManager.getFileConfig("playerdata.yml")
                .getConfigurationSection("players." + player.getUniqueId().toString());
        List<String> subs = playersConfig.getStringList("subscribed-channels");
        subs.remove(channelName);
        playersConfig.set("subscribed-channels", subs);
        configManager.saveConfig("playerdata.yml");
    }
}
