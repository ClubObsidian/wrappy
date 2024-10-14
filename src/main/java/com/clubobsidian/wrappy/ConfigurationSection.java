/*
 *    Copyright 2018-2024 virustotalop
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.clubobsidian.wrappy;

import com.clubobsidian.wrappy.util.NodeUtil;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class ConfigurationSection {

    private static final Map<Class<?>, Object> DEFAULT_VALUES = new HashMap<>() {{
        put(int.class, 0);
        put(Integer.class, 0);
        put(long.class, 0L);
        put(Long.class, 0L);
        put(float.class, 0.0f);
        put(Float.class, 0.0f);
        put(boolean.class, false);
        put(Boolean.class, false);
        put(double.class, 0.0);
        put(Double.class, 0.0);
    }};

    protected ConfigurationNode node;
    protected ConfigurationLoader<?> loader;

    public boolean save() {
        if (this.loader != null && this.loader.canSave()) {
            try {
                this.loader.save(this.node);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public ConfigurationNode getNode() {
        return this.node;
    }

    public ConfigurationLoader getLoader() {
        return this.loader;
    }

    public String getName() {
        return (String) this.node.key();
    }

    public Object get(Object path) {
        return this.get(path, Object.class);
    }

    public <T> T get(Object path, Class<T> clazz) {
        Object def = DEFAULT_VALUES.get(clazz);
        return this.get(path, clazz, def == null ? null : (T) def);
    }

    public <T> T get(Object path, Class<T> clazz, T defaultValue) {
        try {
            ConfigurationNode parsedNode = NodeUtil.parsePath(this.node, path);
            return parsedNode.isNull() ? defaultValue : parsedNode.get(clazz, defaultValue);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getMap(Object path, Class<K> keyType, Class<V> valueType) {
        Map<K, V> map = new LinkedHashMap<>();
        ConfigurationNode parsedNode = NodeUtil.parsePath(this.node, path);
        if (!parsedNode.isNull()) {
            parsedNode.childrenMap().forEach((key1, value) -> {
                try {
                    K key = (K) (keyType.equals(String.class) ?
                            String.valueOf(key1) :
                            key1);
                    map.put(key, value.get(valueType));
                } catch (SerializationException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return Collections.unmodifiableMap(map);
    }

    public String getString(Object path) {
        return this.get(path, String.class);
    }

    public int getInteger(Object path) {
        return this.get(path, Integer.class);
    }

    public long getLong(Object path) {
        return this.get(path, Long.class);
    }

    public float getFloat(Object path) {
        return this.get(path, Float.class);
    }

    public boolean getBoolean(Object path) {
        return this.get(path, Boolean.class);
    }

    public double getDouble(Object path) {
        return this.get(path, Double.class);
    }

    public <T extends Enum> T getEnum(Object path, Class<T> enumClass) {
        return this.get(path, enumClass);
    }

    public URI getURI(Object path) {
        return this.get(path, URI.class);
    }

    public URL getURL(Object path) {
        return this.get(path, URL.class);
    }

    public UUID getUUID(Object path) {
        return this.get(path, UUID.class);
    }

    public Pattern getPattern(Object path) {
        return this.get(path, Pattern.class);
    }

    public List<String> getStringList(Object path) {
        return this.getList(path, String.class);
    }

    public List<Integer> getIntegerList(Object path) {
        return this.getList(path, Integer.class);
    }

    public List<Long> getLongList(Object path) {
        return this.getList(path, Long.class);
    }

    public List<Float> getFloatList(Object path) {
        return this.getList(path, Float.class);
    }

    public List<Boolean> getBooleanList(Object path) {
        return this.getList(path, Boolean.class);
    }

    public List<Double> getDoubleList(Object path) {
        return this.getList(path, Double.class);
    }

    public <T extends Enum> List<T> getEnumList(Object path, Class<T> enumClass) {
        return this.getList(path, enumClass);
    }

    public List<URI> getURIList(Object path) {
        return this.getList(path, URI.class);
    }

    public List<URL> getURLList(Object path) {
        return this.getList(path, URL.class);
    }

    public List<UUID> getUUIDList(Object path) {
        return this.getList(path, UUID.class);
    }

    public List<Pattern> getPatternList(Object path) {
        return this.getList(path, Pattern.class);
    }

    public <T> List<T> getList(Object path, Class<T> clazz) {
        try {
            ConfigurationNode parsedNode = NodeUtil.parsePath(this.node, path);
            return Collections.unmodifiableList(
                    parsedNode.isNull()
                    ? new ArrayList<>()
                    : Objects.requireNonNull(parsedNode.getList(clazz))
            );
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    public ConfigurationSection createConfigurationSection(Object path) {
        return this.getConfigurationSection(path);
    }

    public ConfigurationSection getConfigurationSection(Object path) {
        ConfigurationSection section = new ConfigurationSection();
        section.node = NodeUtil.parsePath(this.node, path);
        return section;
    }

    public void combine(ConfigurationSection from) {
        this.node.mergeFrom(from.node);
    }

    public boolean exists(Object path) {
        return !NodeUtil.parsePath(this.node, path).virtual();
    }

    public <T> void set(Object path, Object toSave) {
        try {
            ConfigurationNode parsed = NodeUtil.parsePath(this.node, path);
            if (toSave instanceof List) {
                List<?> list = ((List<?>) toSave);
                if (!list.isEmpty()) {
                    Class<T> listClazz = (Class<T>) list.get(0).getClass();
                    parsed.setList(listClazz, (List<T>) list);
                }
            } else {
                parsed.set(toSave);
            }
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    public List<Object> getKeys() {
        List<Object> keys = new ArrayList<>();
        this.node.childrenMap().keySet().forEach(n -> keys.add(n));
        return keys;
    }

    public boolean isEmpty() {
        return this.getKeys().isEmpty();
    }

    public boolean hasKey(Object key) {
        return this.getKeys().contains(key);
    }
}