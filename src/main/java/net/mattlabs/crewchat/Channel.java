package net.mattlabs.crewchat;


import net.md_5.bungee.api.ChatColor;

public class Channel {

    private String name, nickname;
    private ChatColor chatColor;
    private boolean autoSubscribe;

    public Channel(String name, String nickname, ChatColor chatColor, boolean autoSubscribe) {
        this.name = name;
        this.nickname = nickname;
        this.chatColor = chatColor;
        this.autoSubscribe = autoSubscribe;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Channel) return name.equalsIgnoreCase(((Channel) object).name);
        else return false;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public boolean isAutoSubscribe() {
        return autoSubscribe;
    }
}
