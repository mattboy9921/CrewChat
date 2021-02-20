package net.mattlabs.crewchat.util;

import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.Config;
import net.mattlabs.crewchat.CrewChat;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;

public class ChannelManager {

    private ConfigurateManager configurateManager;
    private ArrayList<Channel> channels;

    public ChannelManager() {
        configurateManager = CrewChat.getInstance().getConfigurateManager();
        channels = new ArrayList<>();
    }

    public void loadChannels() {
        Config config = configurateManager.get("config.conf");
        channels = (ArrayList<Channel>) config.getChannels();
        for (Channel channel : channels)
            CrewChat.getInstance().getLogger().info("Channel \"" + channel.getName() + "\" added!");
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
