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

import com.clubobsidian.wrappy.util.HashUtil;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.xml.XmlConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Supplier;

public class Configuration extends ConfigurationSection {

    public static Configuration load(File file) {
        return load(file, new HashMap<>());
    }

    public static Configuration load(File file, Map<Class, TypeSerializer> serializers) {
        return new Configuration.Builder().file(file).serializer(serializers).build();
    }

    public static Configuration load(Path path) {
        return load(path, new HashMap<>());
    }

    public static Configuration load(Path path, Map<Class, TypeSerializer> serializers) {
        return new Configuration.Builder().path(path).serializer(serializers).build();
    }

    public static Configuration load(URL url, File backupFile) {
        return load(url, backupFile, new HashMap<>(), true);
    }

    public static Configuration load(URL url, File backupFile, boolean overwrite) {
        return load(url, backupFile, new HashMap<>(), overwrite);
    }

    public static Configuration load(URL url, File backupFile, Map<String, String> requestProperties) {
        return load(url, backupFile, 10000, 10000, requestProperties, true);
    }

    public static Configuration load(URL url, File backupFile,
                                     Map<String, String> requestProperties, boolean overwrite) {
        return load(url, backupFile, 10000, 10000, requestProperties, overwrite);
    }

    public static Configuration load(URL url, File writeTo,
                                     int connectionTimeout, int readTimeout,
                                     Map<String, String> requestProperties, boolean overwrite) {
        return load(url, writeTo, connectionTimeout, readTimeout, requestProperties, overwrite, new HashMap<>());
    }

    public static Configuration load(URL url, File writeTo,
                                     int connectionTimeout, int readTimeout,
                                     Map<String, String> requestProperties, boolean overwrite,
                                     Map<Class, TypeSerializer> serializer) {
        return new Configuration.Builder()
                .url(url, writeTo,
                        connectionTimeout, readTimeout,
                        requestProperties, overwrite)
                .serializer(serializer).build();
    }

    public static Configuration load(InputStream stream, ConfigurationType type) {
        return load(stream, type, new HashMap<>());
    }

    public static Configuration load(InputStream stream,
                                     ConfigurationType type,
                                     Map<Class, TypeSerializer> serializer) {
        return new Configuration.Builder().stream(stream, type).serializer(serializer).build();
    }

    public static Configuration load(ConfigurationLoader<?> loader) {
        Configuration config = new Configuration();
        boolean modified = modifyNode(config, loader);
        return modified ? config : null;
    }

    public static Configuration load(ConfigurationNode node) {
        Configuration config = new Configuration();
        config.node = node;
        return config;
    }

    private static boolean modifyNode(Configuration config, ConfigurationLoader<?> loader) {
        try {
            config.loader = loader;
            config.node = loader.load();
            return true;
        } catch (ConfigurateException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class Builder {

        private static final Map<String, ConfigurationType> EXTENSION_TO_TYPE =
                new HashMap<>() {{
                    put(".yml", ConfigurationType.YAML);
                    put(".conf", ConfigurationType.HOCON);
                    put(".json", ConfigurationType.JSON);
                    put(".xml", ConfigurationType.XML);
                }};
        private static final Map<ConfigurationType, Supplier<AbstractConfigurationLoader.Builder>> TYPE_TO_BUILDER =
                new HashMap<>() {{
                    put(ConfigurationType.YAML,
                            () -> YamlConfigurationLoader.builder().nodeStyle(NodeStyle.BLOCK).indent(2));
                    put(ConfigurationType.HOCON,
                            () -> HoconConfigurationLoader.builder());
                    put(ConfigurationType.JSON,
                            () -> JacksonConfigurationLoader.builder());
                    put(ConfigurationType.XML,
                            () -> XmlConfigurationLoader.builder());
                }};


        private AbstractConfigurationLoader.Builder configBuilder;
        private Runnable finalizingStep;
        private final Map<Class, TypeSerializer> serializers = new LinkedHashMap<>();

        public Builder file(File file) {
            return this.path(file.toPath());
        }

        public Builder path(Path path) {
            this.configBuilder = this.createPathBuilder(path);
            return this;
        }

        public Builder url(URL url, File writeTo,
                           int connectionTimeout, int readTimeout,
                           Map<String, String> requestProperties, boolean overwrite) {
            this.configBuilder = this.createURLLoader(url, writeTo,
                    connectionTimeout, readTimeout,
                    requestProperties, overwrite);
            return this;
        }

        public Builder stream(InputStream stream, ConfigurationType type) {
            this.configBuilder = this.createStreamBuilder(stream, type);
            return this;
        }

        public Builder serializer(Class clazz, TypeSerializer serializer) {
            this.serializers.put(clazz, serializer);
            return this;
        }

        public Builder serializer(Map<Class, TypeSerializer> serializers) {
            this.serializers.putAll(serializers);
            return this;
        }

        public Configuration build() {
            Configuration config = new Configuration();
            TypeSerializerCollection.Builder serializerBuilder = TypeSerializerCollection.defaults().childBuilder();
            //Apply serializers
            for (Entry<Class, TypeSerializer> entry : this.serializers.entrySet()) {
                serializerBuilder.register(entry.getKey(), entry.getValue());
            }
            this.configBuilder.defaultOptions(
                    this.configBuilder.defaultOptions()
                            .shouldCopyDefaults(true)
                            .serializers(serializerBuilder.build())
            );
            //Build and apply loader
            boolean modified = modifyNode(config, this.configBuilder.build());
            if (this.finalizingStep != null) {
                this.finalizingStep.run();
            }
            return modified ? config : null;
        }

        private AbstractConfigurationLoader.Builder createStreamBuilder(InputStream stream, ConfigurationType type) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            AbstractConfigurationLoader.Builder builder = this.getBuilderFromType(type).source(() -> reader);
            //This isn't really elegant but the streams should be closed after the file is loaded in
            this.finalizingStep = () -> {
                try {
                    reader.close();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            return builder;
        }

        private AbstractConfigurationLoader.Builder createURLLoader(URL url, File writeTo,
                                                                    int connectionTimeout, int readTimeout,
                                                                    Map<String, String> requestProperties, boolean overwrite) {
            try {
                if (!writeTo.exists() && (writeTo.length() == 0 || !overwrite)) {
                    if (!writeTo.exists()) {
                        writeTo.createNewFile();
                    }

                    URLConnection connection = url.openConnection();
                    connection.setConnectTimeout(connectionTimeout);
                    connection.setReadTimeout(readTimeout);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    Iterator<Entry<String, String>> it = requestProperties.entrySet().iterator();
                    while (it.hasNext()) {
                        Entry<String, String> next = it.next();
                        connection.setRequestProperty(next.getKey(), next.getValue());
                    }

                    InputStream inputStream = connection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    StringBuilder sb = new StringBuilder();
                    int read;
                    while ((read = reader.read()) != -1) {
                        sb.append((char) read);
                    }

                    byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
                    byte[] tempMD5 = HashUtil.getMD5(data);
                    byte[] backupMD5 = HashUtil.getMD5(writeTo);
                    if (data.length > 0 && tempMD5 != backupMD5) {
                        if (writeTo.exists()) {
                            writeTo.delete();
                        }
                        writeTo.createNewFile();
                        Files.write(writeTo.toPath(), data, StandardOpenOption.WRITE);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (writeTo.exists()) {
                return this.createPathBuilder(writeTo.toPath());
            }
            return null;
        }

        private AbstractConfigurationLoader.Builder createPathBuilder(Path path) {
            String fileName = path.getFileName().toString().toLowerCase(Locale.ROOT);
            ConfigurationType type = null;
            for (Entry<String, ConfigurationType> entry : EXTENSION_TO_TYPE.entrySet()) {
                if (fileName.endsWith(entry.getKey())) {
                    type = entry.getValue();
                    break;
                }
            }
            if (type == null) {
                throw new UnknownFileTypeException(fileName);
            }
            return this.getBuilderFromType(type).path(path);
        }

        private AbstractConfigurationLoader.Builder getBuilderFromType(ConfigurationType type) {
            Supplier<AbstractConfigurationLoader.Builder> supplier = TYPE_TO_BUILDER.get(type);
            return supplier == null ? null : supplier.get();
        }
    }
}