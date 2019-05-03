package net.mattlabs.crewchat.util;

import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.CrewChat;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;

public class ChannelManager {

    private ConfigManager configManager;
    private ArrayList<Channel> channels;

    public ChannelManager() {
        configManager = CrewChat.getInstance().getConfigManager();
        channels = new ArrayList<>();
    }

    public void loadChannels() {
        ConfigurationSection channelsConfig = configManager.getFileConfig("config.yml")
                .getConfigurationSection("channels");
        for (String key : channelsConfig.getKeys(false)) {
            Channel channel = new Channel(key, // Channel Name
                    ChatColor.valueOf(channelsConfig.getConfigurationSection(key).getString("chatcolor")), // Chat Color
                    channelsConfig.getConfigurationSection(key).getBoolean("autosubscribe")); // Auto Subscribe
            CrewChat.getInstance().getLogger().info("Channel \"" + key + "\" added!");
            channels.add(channel);
        }
    }

    public void reloadChannels() {
        channels.clear();
        loadChannels();
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public ArrayList<String> getChannelNames() {
        ArrayList<String> channelNames = new ArrayList<>();
        for (Channel channel : channels) channelNames.add(channel.getName());
        return channelNames;
    }

    public Channel channelFromString(String channelName) {
        Channel channel = new Channel(channelName, null, false);
        if (channels.contains(channel)) return channels.get(channels.indexOf(channel));
        else return null;
    }

    public ChatColor getChatColor(Channel channel) {
        if (channels.contains(channel)) return channels.get(channels.indexOf(channel)).getChatColor();
        else return null;
    }
}
