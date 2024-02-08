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

import com.clubobsidian.wrappy.util.HashUtil;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

public class Configuration extends ConfigurationSection {

	private static final Map<String, ConfigurationType> EXTENSION_TO_TYPE = new HashMap<String, ConfigurationType>() {{
		put(".yml", ConfigurationType.YAML);
		put(".conf", ConfigurationType.HOCON);
		put(".json", ConfigurationType.JSON);
		put(".xml", ConfigurationType.XML);
	}};


	public static Configuration load(File file) {
		return Configuration.load(file.toPath());
	}

	public static Configuration load(Path path) {
		return new Configuration.Builder().path(path).build();
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

	public static Configuration load(URL url, File writeTo,
									 int connectionTimeout, int readTimeout,
									 Map<String,String> requestProperties, boolean overwrite) {
		return new Configuration.Builder().url(url, writeTo,
				connectionTimeout, readTimeout,
				requestProperties, overwrite).build();
	}

	public static Configuration load(InputStream stream, ConfigurationType type) {
		ConfigurationLoader<?> loader = null;
		Configuration config = new Configuration();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		Callable<BufferedReader> callable = () -> reader;
		if(type == ConfigurationType.YAML) {
			loader = YamlConfigurationLoader
					.builder()
					.source(callable)
					.nodeStyle(NodeStyle.BLOCK)
					.indent(2)
					.build();
		} else if(type == ConfigurationType.HOCON) {
			loader = HoconConfigurationLoader
					.builder()
					.source(callable)
					.build();
		} else if(type == ConfigurationType.JSON) {
			loader = JacksonConfigurationLoader
					.builder()
					.source(callable)
					.build();
		} else {
			loader = XmlConfigurationLoader
					.builder()
					.source(callable)
					.build();
		}
		boolean modified = modifyNode(config, loader);
		try {
			reader.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!modified) {
			return null;
		}

		return config;
	}

	public static Configuration load(ConfigurationLoader<?> loader) {
		Configuration config = new Configuration();
		boolean modified = modifyNode(config, loader);
		if(!modified) {
			return null;
		}
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

		private ConfigurationLoader<?> loader;
		private ConfigurationType type;

		public Builder type(ConfigurationType type) {
			this.type = type;
			return this;
		}

		public Builder path(Path path) {
			this.loader = this.createPathLoader(path);
			return this;
		}

		public Builder url(URL url, File writeTo,
						   int connectionTimeout, int readTimeout,
						   Map<String,String> requestProperties, boolean overwrite) {
			this.loader = this.createURLLoader(url, writeTo,
					connectionTimeout, readTimeout,
					requestProperties, overwrite);
			return this;
		}

		private ConfigurationLoader<?> createURLLoader(URL url, File writeTo,
													   int connectionTimeout, int readTimeout,
													   Map<String, String> requestProperties, boolean overwrite) {
			try  {
				if(writeTo != null && writeTo.exists() && writeTo.length() > 0 && !overwrite) {
					return Configuration.load(writeTo).loader;
				}
				if(!writeTo.exists()) {
					writeTo.createNewFile();
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
				InputStreamReader reader = new InputStreamReader(inputStream);
				StringBuilder sb = new StringBuilder();
				int read;
				while((read = reader.read()) != -1) {
					sb.append((char) read);
				}

				byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
				byte[] tempMD5 = HashUtil.getMD5(data);
				byte[] backupMD5 = HashUtil.getMD5(writeTo);
				if(data.length > 0 && tempMD5 != backupMD5) {
					if(writeTo.exists()) {
						writeTo.delete();
					}
					writeTo.createNewFile();
					Files.write(writeTo.toPath(), data, StandardOpenOption.WRITE);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(writeTo.exists()) {
				return this.createPathLoader(writeTo.toPath());
			}
			return null;
		}

		public Configuration build() {
			return Configuration.load(this.loader);
		}

		private AbstractConfigurationLoader createPathLoader(Path path) {
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
			return this.getBuilderFromType(type).path(path).build();
		}

		private AbstractConfigurationLoader.Builder getBuilderFromType(ConfigurationType type) {
			switch(type) {
				case YAML:
					return YamlConfigurationLoader.builder().nodeStyle(NodeStyle.BLOCK).indent(2);
				case HOCON:
					return HoconConfigurationLoader.builder();
				case JSON:
					return JacksonConfigurationLoader.builder();
				case XML:
					return XmlConfigurationLoader.builder();
			}
			return null;
		}
	}
}