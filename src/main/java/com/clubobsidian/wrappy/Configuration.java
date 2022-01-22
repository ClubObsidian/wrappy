/*
 *    Copyright 2021 Club Obsidian and contributors.
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

import com.clubobsidian.wrappy.exception.UninitializedPropertiesException;
import com.clubobsidian.wrappy.exception.UnknownFileTypeException;
import com.clubobsidian.wrappy.source.ConfigLoaderSource;
import com.clubobsidian.wrappy.source.InputStreamConfigSource;
import com.clubobsidian.wrappy.source.PathConfigSource;
import com.clubobsidian.wrappy.source.URLConfigSource;
import com.clubobsidian.wrappy.util.HashUtil;
import org.apache.commons.io.FileUtils;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.xml.XmlConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Configuration extends ConfigurationSection {

	public static Configuration load(File file) {
		try {
			return builder().file(file).build(ConfigurationType.fromString(file.getName()));
		} catch(UninitializedPropertiesException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Configuration load(Path path) {
		try {
			return builder().path(path).build(ConfigurationType.fromString(path.getFileName().toString()));
		} catch(UninitializedPropertiesException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Configuration load(URL url, File backupFile) {
		return load(url, backupFile, new HashMap<>(), true);
	}

	public static Configuration load(URL url, File backupFile, boolean overwrite) {
		return load(url, backupFile, new HashMap<>(), overwrite);
	}

	public static Configuration load(URL url, File backupFile, Map<String,String> requestProperties) {
		return load(url, backupFile, 10000, 10000, requestProperties, true);
	}
	
	public static Configuration load(URL url, File backupFile, Map<String,String> requestProperties, boolean overwrite) {
		return load(url, backupFile, 10000, 10000, requestProperties, overwrite);
	}
	
	public static Configuration load(URL url, File file, int connectionTimeout, int readTimeout, Map<String, String> requestProperties, boolean overwrite) {
		try  {
			if(file != null && file.exists() && file.length() > 0 && !overwrite) {
				return Configuration.load(file);
			}
			if(!file.exists()) {
				file.createNewFile();
			}

			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			Iterator<Entry<String,String>> it = requestProperties.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String,String> next = it.next();
				connection.setRequestProperty(next.getKey(), next.getValue());
			}

			InputStream inputStream = connection.getInputStream();
			Reader reader = new InputStreamReader(inputStream);
			StringBuilder sb = new StringBuilder();
			int read;
			while((read = reader.read()) != -1) {
				sb.append((char) read);
			}

			byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
			byte[] tempMD5 = HashUtil.getMD5(data);
			byte[] backupMD5 = HashUtil.getMD5(file);
			if(data.length > 0 && tempMD5 != backupMD5) {
				if(file.exists()) {
					file.delete();
				}

				file.createNewFile();
				FileUtils.writeByteArrayToFile(file, data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(file.exists()) {
			return Configuration.load(file);
		}

		return new Configuration();
	}

	public static Configuration load(InputStream stream, ConfigurationType type) {
		try {
			return builder().stream(stream).build(type);
		} catch(UninitializedPropertiesException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Configuration load(ConfigurationLoader<?> loader) {
		try {
			return builder().loader(loader).build(ConfigurationType.fromClass(loader.getClass()));
		} catch(UninitializedPropertiesException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private final Options opts = new Options();
		private ConfigSource<?> source;

		Builder() {

		}

		public Builder path(Path path) {
			return this.source(new PathConfigSource(path));
		}

		public Builder file(File file) {
			return this.path(file.toPath());
		}

		public Builder url(URL url) {
			return this.source(new URLConfigSource(url));
		}

		public Builder stream(InputStream stream) {
			return this.source(new InputStreamConfigSource(stream));
		}

		public Builder source(ConfigSource<?> source) {
			this.source = source;
			return this;
		}

		public Builder loader(ConfigurationLoader<?> loader) {
			this.source = new ConfigLoaderSource(loader);
			return this;
		}

		public Options options() {
			return this.opts;
		}

		public Configuration build(ConfigurationType type) throws UninitializedPropertiesException {
			if(this.source == null) {
				throw new UninitializedPropertiesException();
			}

			Configuration config = new Configuration();
			AbstractConfigurationLoader.Builder<?,?> builder;
			if(type == ConfigurationType.YAML) {
				builder = YamlConfigurationLoader
						.builder()
						.nodeStyle(NodeStyle.BLOCK)
						.indent(2);
			} else if(type == ConfigurationType.HOCON) {
				builder = HoconConfigurationLoader.builder();
			} else if(type == ConfigurationType.JSON) {
				builder = JacksonConfigurationLoader.builder();
			} else if(type == ConfigurationType.XML) {
				builder = XmlConfigurationLoader.builder();
			} else {
				throw new UnknownFileTypeException();
			}

			ConfigurationLoader<?> loader = this.source.load(this.opts, builder);
			loader.defaultOptions().shouldCopyDefaults(true);

			try {
				config.loader = loader;
				config.node = loader.load();
				return config;
			} catch (ConfigurateException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public static class Options {

		private int connectTimeout = 10000;
		private int readTimeout = 10000;
		private final Map<String, String> requestProperties = new HashMap<>();

		Options() {

		}

		public Options setConnectTimeout(int timeout) {
			this.connectTimeout = timeout;
			return this;
		}

		public Options setReadTimeout(int timeout) {
			this.readTimeout = timeout;
			return this;
		}

		public Options addRequestProperty(String key, String value) {
			this.requestProperties.put(key, value);
			return this;
		}

		public int getConnectTimeout() {
			return this.connectTimeout;
		}

		public int getReadTimeout() {
			return this.readTimeout;
		}

		public Map<String, String> getRequestProperties() {
			return this.requestProperties;
		}
	}
}