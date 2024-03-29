package net.mattlabs.crewchat;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
@ConfigSerializable
public class PlayerData {

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

            CrewChat Player Data
            By Mattboy9921
            https://github.com/mattboy9921/CrewChat

            This file should not be hand edited and is auto generated by the plugin.

            #######################################################################################################

            Config version. Do not change this!""")
    private int schemaVersion = 0;

    @Setting(value = "players")
    @Comment("\n ")
    private Map<UUID, Chatter> chattersMap = new HashMap<>();

    public ArrayList<Chatter> getChatters() {

        // Convert map to arraylist
        ArrayList<Chatter> chatters = new ArrayList<>();
        chattersMap.forEach((uuid, chatter) -> chatters.add(new Chatter(uuid, chatter.getActiveChannel(), chatter.getSubscribedChannels(), chatter.getMutedPlayers(), chatter.getStatus())));
        return chatters;
    }

    public void addChatter(Chatter chatter) {
        chattersMap.put(chatter.getUuid(), chatter);
    }

    public void setChatter(Chatter chatter) {
        chattersMap.replace(chatter.getUuid(), chatter);
    }
}
