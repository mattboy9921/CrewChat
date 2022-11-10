package net.mattlabs.crewchat;

import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.*;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public class Config {

    // Header fields
    @Setting(value = "_schema-version")
    @Comment("""
            #######################################################################################################
                ________  ________  _______   ___       __   ________  ___  ___  ________  _________  \s
               |\\   ____\\|\\   __  \\|\\  ___ \\ |\\  \\     |\\  \\|\\   ____\\|\\  \\|\\  \\|\\   __  \\|\\___   ___\\\s
               \\ \\  \\___|\\ \\  \\|\\  \\ \\   __/|\\ \\  \\    \\ \\  \\ \\  \\___|\\ \\  \\\\\\  \\ \\  \\|\\  \\|___ \\  \\_|\s
                \\ \\  \\    \\ \\   _  _\\ \\  \\_|/_\\ \\  \\  __\\ \\  \\ \\  \\    \\ \\   __  \\ \\   __  \\   \\ \\  \\ \s
                 \\ \\  \\____\\ \\  \\\\  \\\\ \\  \\_|\\ \\ \\  \\|\\__\\_\\  \\ \\  \\____\\ \\  \\ \\  \\ \\  \\ \\  \\   \\ \\  \\\s
                  \\ \\_______\\ \\__\\\\ _\\\\ \\_______\\ \\____________\\ \\_______\\ \\__\\ \\__\\ \\__\\ \\__\\   \\ \\__\\
                   \\|_______|\\|__|\\|__|\\|_______|\\|____________|\\|_______|\\|__|\\|__|\\|__|\\|__|    \\|__|

            CrewChat Configuration
            By Mattboy9921
            https://github.com/mattboy9921/CrewChat

            This is the main configuration file for CrewChat.

            #######################################################################################################

            Config version. Do not change this!""")
    private int schemaVersion = 0;

    @Setting(value = "enable-discordsrv")
    @Comment("\nEnable DiscordSRV integration.")
    private boolean enableDiscordSRV = false;

    public boolean isEnableDiscordSRV() {
        return enableDiscordSRV;
    }

    @Comment("\nShow channel names on Discord to in game messages.")
    private boolean showDiscordChannelNameInGame = false;

    public boolean isShowDiscordChannelNameInGame() {
        return showDiscordChannelNameInGame;
    }

    @Comment("\nAllow color codes/MiniMessage tags in chat globally.")
    private boolean allowColor = false;

    public boolean isAllowColor() {
        return allowColor;
    }

    @Comment("\nTime parties exist with nobody in them (in minutes).")
    private int partyTimeout = 10;

    public int getPartyTimeout() {
        return partyTimeout;
    }

    @Setting(value = "channels")
    @Comment("""

            Channel Configuration
            Define each channel here. Text colors can be either a named color or a hex code surrounded by quotes ("#ff2acb").""")
    private Map<String, Channel> channelsMap = new HashMap<>(Collections.singletonMap("Global", new Channel("Global", "Global chat channel", NamedTextColor.WHITE, true, false, false, false)));

    public List<Channel> getChannels() {

        // Convert map to arraylist
        ArrayList<Channel> channels = new ArrayList<>();
        channelsMap.forEach((name, channel) -> channels.add(new Channel(name, channel.getDescription(), channel.getTextColor(), channel.isAutoSubscribe(), channel.isShowChannelNameInGame(), channel.isShowChannelNameDiscord(), channel.isExcludeFromDiscord())));
        return channels;
    }

    public void setChannel(String oldChannelName, Channel updatedChannel) {
        channelsMap.remove(oldChannelName);
        channelsMap.put(updatedChannel.getName(), updatedChannel);
    }
}
