package net.mattlabs.crewchat;

import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.*;

@ConfigSerializable
public class Config {

    @Setting(value = "_mattIsAwesome")
    @Comment("CrewChat Configuration\n" +
            "By Mattboy9921\n" +
            "https://github.com/mattboy9921/CrewChat")
    private boolean _mattIsAwesome = true;

    @Setting(value = "channels")
    @Comment("\nChannel Configuration\n" +
            "Define each channel here. Text colors can be either a named color or a hex code surrounded by quotes (\"#ff2acb\").")
    private Map<String, Channel> channelsMap = new HashMap<>(Collections.singletonMap("Global", new Channel("Global", NamedTextColor.WHITE, true)));

    public List<Channel> getChannels() {

        // Convert map to arraylist
        ArrayList<Channel> channels = new ArrayList<>();
        channelsMap.forEach((name, channel) -> channels.add(new Channel(name, channel.getTextColor(), channel.isAutoSubscribe())));
        return channels;
    }
}
