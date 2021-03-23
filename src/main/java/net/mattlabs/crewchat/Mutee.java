package net.mattlabs.crewchat;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.LocalDateTime;
import java.util.UUID;

@ConfigSerializable
public class Mutee {

    private UUID uuid;
    private String prefix, name;
    private LocalDateTime time;

    // Empty constructor for Configurate
    public Mutee() {}

    public Mutee(UUID uuid, String prefix, String name) {
        this.uuid = uuid;
        this.prefix = prefix;
        this.name = name;
        time = LocalDateTime.now();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void updateTime() {
        time = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof Mutee) return uuid.equals(((Mutee) object).uuid);
        else return false;
    }
}