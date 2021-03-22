package net.mattlabs.crewchat.util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.mattlabs.crewchat.CrewChat;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.util.function.Predicate;

public class TextColorSerializer extends ScalarSerializer<TextColor> {
    public static final TextColorSerializer INSTANCE = new TextColorSerializer();

    private TextColorSerializer() {
        super(TextColor.class);
    }

    @Override
    public TextColor deserialize(Type type, Object obj) throws SerializationException {
        String value = obj.toString();
        try {
            if (!value.startsWith("#")) value = NamedTextColor.NAMES.value(value).asHexString();
        }
        catch (NullPointerException e) {
            CrewChat.getInstance().getLogger().severe("Failed to convert TextColor: " + value);
            throw new SerializationException();
        }
        final TextColor result;
        result = TextColor.fromHexString(value);
        return result;
    }

    @Override
    protected Object serialize(TextColor item, Predicate<Class<?>> typeSupported) {
        return item.toString();
    }
}
