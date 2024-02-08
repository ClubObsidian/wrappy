package com.clubobsidian.wrappy.test.mock;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class FooBarSerializerMock implements TypeSerializer<FooBarMock> {

    @Override
    public FooBarMock deserialize(Type type, ConfigurationNode node) {
        return new FooBarMock(node.getString());
    }

    @Override
    public void serialize(Type type, @Nullable FooBarMock obj, ConfigurationNode node) throws SerializationException {
        node.set(obj.getFoobar());
    }
}
