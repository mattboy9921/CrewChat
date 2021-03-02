package net.mattlabs.crewchat.util;

import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class LocalDateTimeSerializer extends ScalarSerializer<LocalDateTime> {
    public static final LocalDateTimeSerializer INSTANCE = new LocalDateTimeSerializer();

    private LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(Type type, Object obj) throws SerializationException {
        final String value = obj.toString();
        final LocalDateTime result;
        result = LocalDateTime.parse(value);
        return result;
    }

    @Override
    protected Object serialize(LocalDateTime item, Predicate<Class<?>> typeSupported) {
        return item.toString();
    }
}
