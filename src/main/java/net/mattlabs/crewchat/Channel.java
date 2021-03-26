package net.mattlabs.crewchat;


import net.kyori.adventure.text.format.TextColor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Channel {

    private transient String name;
    private String description;
    private TextColor textColor;
    private boolean autoSubscribe;

    // Empty constructor for Configurate
    public Channel() { }

    public Channel(String name, String description, TextColor textColor, boolean autoSubscribe) {
        this.name = name;
        this.description = description;
        this.textColor = textColor;
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

    public String getDescription() {
        return description;
    }

    public TextColor getTextColor() {
        return textColor;
    }

    public boolean isAutoSubscribe() {
        return autoSubscribe;
    }
}
