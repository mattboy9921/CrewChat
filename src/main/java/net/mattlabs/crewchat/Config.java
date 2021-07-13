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
    @Comment("#######################################################################################################\n" +
            "    ________  ________  _______   ___       __   ________  ___  ___  ________  _________   \n" +
            "   |\\   ____\\|\\   __  \\|\\  ___ \\ |\\  \\     |\\  \\|\\   ____\\|\\  \\|\\  \\|\\   __  \\|\\___   ___\\ \n" +
            "   \\ \\  \\___|\\ \\  \\|\\  \\ \\   __/|\\ \\  \\    \\ \\  \\ \\  \\___|\\ \\  \\\\\\  \\ \\  \\|\\  \\|___ \\  \\_| \n" +
            "    \\ \\  \\    \\ \\   _  _\\ \\  \\_|/_\\ \\  \\  __\\ \\  \\ \\  \\    \\ \\   __  \\ \\   __  \\   \\ \\  \\  \n" +
            "     \\ \\  \\____\\ \\  \\\\  \\\\ \\  \\_|\\ \\ \\  \\|\\__\\_\\  \\ \\  \\____\\ \\  \\ \\  \\ \\  \\ \\  \\   \\ \\  \\ \n" +
            "      \\ \\_______\\ \\__\\\\ _\\\\ \\_______\\ \\____________\\ \\_______\\ \\__\\ \\__\\ \\__\\ \\__\\   \\ \\__\\\n" +
            "       \\|_______|\\|__|\\|__|\\|_______|\\|____________|\\|_______|\\|__|\\|__|\\|__|\\|__|    \\|__|\n\n" +

            "CrewChat Configuration\n" +
            "By Mattboy9921\n" +
            "https://github.com/mattboy9921/CrewChat\n\n" +

            "This is the main configuration file for CrewChat.\n\n" +

            "#######################################################################################################\n\n" +

            "Config version. Do not change this!")
    private int schemaVersion = 0;

    @Comment("\nShow channel names in Discord messages.")
    public boolean showChannelNamesDiscord = false;

    @Setting(value = "channels")
    @Comment("\nChannel Configuration\n" +
            "Define each channel here. Text colors can be either a named color or a hex code surrounded by quotes (\"#ff2acb\").")
    private Map<String, Channel> channelsMap = new HashMap<>(Collections.singletonMap("Global", new Channel("Global", "Global chat channel", NamedTextColor.WHITE, true)));

    public List<Channel> getChannels() {

        // Convert map to arraylist
        ArrayList<Channel> channels = new ArrayList<>();
        channelsMap.forEach((name, channel) -> channels.add(new Channel(name, channel.getDescription(), channel.getTextColor(), channel.isAutoSubscribe())));
        return channels;
    }
}
