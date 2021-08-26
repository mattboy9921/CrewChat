package net.mattlabs.crewchat;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class Party extends Channel {

    public Party(String name) {
        this(name, NamedTextColor.WHITE);
    }

    public Party(String name, TextColor textColor) {
        super(name, "Party", textColor, false, true, false, true);
    }
}
