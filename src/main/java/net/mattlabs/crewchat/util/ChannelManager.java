package net.mattlabs.crewchat.util;

import net.kyori.adventure.text.format.TextColor;
import net.mattlabs.crewchat.Channel;
import net.mattlabs.crewchat.Config;
import net.mattlabs.crewchat.CrewChat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChannelManager {

    private final ConfigurateManager configurateManager = CrewChat.getInstance().getConfigurateManager();

    private final Map<String, Channel> channels = new CaseInsensitiveHashMap();

    private void loadChannel(Channel channel) {
        Config config = configurateManager.get("config.conf");
        ArrayList<Channel> channels = (ArrayList<Channel>) config.getChannels();
        channels.forEach(configChannel -> {
            if (configChannel.equals(channel)) this.channels.put(channel.getName(), channel);
            CrewChat.getInstance().getLogger().info("Channel \"" + channel.getName() + "\" added!");
        });
    }

    public void loadChannels() {
        Config config = configurateManager.get("config.conf");
        config.getChannels().forEach(channel -> {
            channels.put(channel.getName(), channel);
            CrewChat.getInstance().getLogger().info("Channel \"" + channel.getName() + "\" added!");
        });
    }

    public void reloadChannel(Channel channel) {
        channels.remove(channel.getName());
        loadChannel(channel);
    }

    public void reloadChannels() {
        channels.clear();
        loadChannels();
    }

    public void addChannel(Channel channel) {
        channels.put(channel.getName(), channel);
        // TODO: Make this channel/party sensitive
        CrewChat.getInstance().getLogger().info("Party \"" + channel.getName() + "\" added!");
    }

    public void removeChannel(Channel channel) {
        if (channels.containsKey(channel.getName())) {
            channels.remove(channel.getName());
            // TODO: Make this channel/party sensitive
            CrewChat.getInstance().getLogger().info("Party \"" + channel.getName() + "\" removed!");
        }
        else {
            // TODO: Make this channel/party sensitive
            CrewChat.getInstance().getLogger().warning("Party \"" + channel.getName() + "\" could not be removed!");
        }
    }

    public ArrayList<Channel> getChannels() {
        return new ArrayList<>(channels.values());
    }

    public ArrayList<String> getChannelNames() {
        ArrayList<String> channelNames = new ArrayList<>();
        channels.values().forEach(channel -> channelNames.add(channel.getName()));
        return channelNames;
    }

    public Channel channelFromString(String channelName) {
        return channels.get(channelName);
    }

    public TextColor getTextColor(Channel channel) {
        if (channels.containsKey(channel.getName())) return channels.get(channel.getName()).getTextColor();
        else return null;
    }

    private static class CaseInsensitiveHashMap extends HashMap<String, Channel> {
        @Override
        public Channel put(String key, Channel value) {
            return super.put(key.toLowerCase(), value);
        }

        @Override
        public Channel get(Object key) {
            return super.get(key.toString().toLowerCase());
        }

        @Override
        public boolean containsKey(Object key) {
            return super.containsKey(key.toString().toLowerCase());
        }
    }
}
