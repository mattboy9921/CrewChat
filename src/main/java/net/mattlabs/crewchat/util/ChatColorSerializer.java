package net.mattlabs.crewchat.util;

import net.md_5.bungee.api.ChatColor;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.function.Predicate;

public class ChatColorSerializer extends ScalarSerializer<ChatColor> {
    public static final ChatColorSerializer INSTANCE = new ChatColorSerializer();

    private ChatColorSerializer() {
        super(ChatColor.class);
    }

    @Override
    public ChatColor deserialize(Type type, Object obj) throws SerializationException {
        final String value = obj.toString();
        final ChatColor result;
        result = ChatColor.of(value);
        return result;
    }

    @Override
    protected Object serialize(ChatColor item, Predicate<Class<?>> typeSupported) {
        return item.getName();
    }
}
