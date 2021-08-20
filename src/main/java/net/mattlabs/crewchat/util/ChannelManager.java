package net.mattlabs.crewchat.util;

import net.kyori.adventure.text.format.TextColor;
import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.Config;
import net.mattlabs.crewchat.CrewChat;

import java.util.ArrayList;

public class ChannelManager {

    private final ConfigurateManager configurateManager = CrewChat.getInstance().getConfigurateManager();

    private ArrayList<Channel> channels  = new ArrayList<>();

    private void loadChannel(Channel channel) {
        Config config = configurateManager.get("config.conf");
        ArrayList<Channel> channels = (ArrayList<Channel>) config.getChannels();
        for (Channel listChannel : channels)
            if (!this.channels.contains(listChannel)) {
                this.channels.add(channel);
                CrewChat.getInstance().getLogger().info("Channel \"" + channel.getName() + "\" added!");
            }
    }

    public void loadChannels() {
        Config config = configurateManager.get("config.conf");
        channels = (ArrayList<Channel>) config.getChannels();
        for (Channel channel : channels)
            CrewChat.getInstance().getLogger().info("Channel \"" + channel.getName() + "\" added!");
    }

    public void reloadChannel(Channel channel) {
        channels.remove(channel);
        loadChannel(channel);
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
        Channel channel = new Channel(channelName);
        if (channels.contains(channel)) return channels.get(channels.indexOf(channel));
        else return null;
    }

    public TextColor getTextColor(Channel channel) {
        if (channels.contains(channel)) return channels.get(channels.indexOf(channel)).getTextColor();
        else return null;
    }
}
