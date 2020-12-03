/*  
   Copyright 2020 Club Obsidian and contributors.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.clubobsidian.wrappy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.xml.XmlConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import com.clubobsidian.wrappy.util.HashUtil;

public class Configuration extends ConfigurationSection {

	public static Configuration load(File file) {
		return Configuration.load(file.toPath());
	}
	
	public static Configuration load(Path path) {
		Configuration config = new Configuration();
		try {
			String fileName = path.getFileName().toString();
			ConfigurationLoader<?> loader = null;
			
			if(fileName.endsWith(".yml")) {
				loader = YamlConfigurationLoader
						.builder()
						.nodeStyle(NodeStyle.BLOCK)
						.indent(2)
						.path(path)
						.build();
			} else if(fileName.endsWith(".conf")) {
				loader = HoconConfigurationLoader
						.builder()
						.path(path)
						.build();
			} else if(fileName.endsWith(".json")) {
				loader = JacksonConfigurationLoader
						.builder()
						.path(path)
						.build();
			} else if(fileName.endsWith(".xml")) {
				loader = XmlConfigurationLoader
						.builder()
						.path(path)
						.build();
			} else {
				throw new UnknownFileTypeException(fileName);
			}
		
			config.loader = loader;
			config.node = loader.load();
			return config;
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		return config;
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
	
	public static Configuration load(URL url, File file, int connectionTimeout, int readTimeout, Map<String,String> requestProperties, boolean overwrite) {
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
			InputStreamReader reader = new InputStreamReader(inputStream);
			StringBuilder sb = new StringBuilder();
			int read = -1;
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
			return null;
		}
		
		if(file.exists()) {
			return Configuration.load(file);
		}

		return new Configuration();
	}

	public static Configuration load(InputStream stream, ConfigurationType type) {	
		ConfigurationLoader<?> loader = null;
		Configuration config = new Configuration();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try {
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
			config.loader = loader;
			config.node = loader.load();
		} catch(IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				reader.close();
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return config;
	}
}