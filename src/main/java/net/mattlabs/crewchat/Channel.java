package net.mattlabs.crewchat;


import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Channel {

    private transient String name = "Unnamed";
    private String description = "No description";
    private TextColor textColor = NamedTextColor.WHITE;
    private boolean autoSubscribe = false;
    private boolean showChannelNameDiscord = false;

    // Empty constructor for Configurate
    public Channel() { }

    public Channel(String name, String description, TextColor textColor, boolean autoSubscribe, boolean showChannelNameDiscord) {
        this.name = name;
        this.description = description;
        this.textColor = textColor;
        this.autoSubscribe = autoSubscribe;
        this.showChannelNameDiscord = showChannelNameDiscord;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Channel) return name.equalsIgnoreCase(((Channel) object).name);
        else return false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TextColor getTextColor() {
        return textColor;
    }

    public boolean isAutoSubscribe() {
        return autoSubscribe;
    }

    public boolean isShowChannelNameDiscord() {
        return showChannelNameDiscord;
    }
}
