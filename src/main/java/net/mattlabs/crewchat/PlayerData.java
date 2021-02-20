package net.mattlabs.crewchat;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ConfigSerializable
public class PlayerData {

    @Setting(value = "_mattIsAwesome")
    @Comment("CrewChat Player Data\n" +
            "By Mattboy9921\n" +
            "https://github.com/mattboy9921/CrewChat\n\n" +
            "This file should not be hand edited and is auto generated by the plugin.")
    private boolean _mattIsAwesome = true;

    @Setting(value = "players")
    @Comment("\n ")
    private Map<UUID, Chatter> chattersMap = new HashMap<>();

    public ArrayList<Chatter> getChatters() {

        // Convert map to arraylist
        ArrayList<Chatter> chatters = new ArrayList<>();
        chattersMap.forEach((uuid, chatter) -> chatters.add(new Chatter(uuid, chatter.getActiveChannel(), chatter.getSubscribedChannels(), chatter.getStatus())));
        return chatters;
    }

    public void addChatter(Chatter chatter) {
        chattersMap.put(chatter.getUuid(), chatter);
    }

    public void setChatter(Chatter chatter) {
        chattersMap.replace(chatter.getUuid(), chatter);
    }
}
