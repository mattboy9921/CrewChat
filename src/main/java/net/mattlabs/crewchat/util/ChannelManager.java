package net.mattlabs.crewchat.util;

import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.CrewChat;
import org.bukkit.ChatColor;
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
                    channelsConfig.getConfigurationSection(key).getString("nickname"), // Nickname
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

    public Channel channelFromString(String channelName) {
        Channel channel = new Channel(channelName, null, null, false);
        if (channels.contains(channel)) return channels.get(channels.indexOf(channel));
        else return null;
    }

    public Channel channelFromNickname(String nickname) {
        for (Channel channel : channels) {
            if (channel.getNickname().equalsIgnoreCase(nickname))
                    return channel;
        }
        return null;
    }

    public ChatColor getChatColor(Channel channel) {
        if (channels.contains(channel)) return channels.get(channels.indexOf(channel)).getChatColor();
        else return null;
    }
}
