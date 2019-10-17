/*  
   Copyright 2018 Club Obsidian and contributors.

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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;

import org.yaml.snakeyaml.DumperOptions.FlowStyle;

import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.json.JSONConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.xml.XMLConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

public class Configuration extends ConfigurationSection {

	public static Configuration load(File file)
	{
		return Configuration.load(file.toPath());
	}
	
	public static Configuration load(Path path)
	{
		Configuration config = new Configuration();
		try
		{
			String fileName = path.getFileName().toString();
			ConfigurationLoader<?> loader = null;
			
			if(fileName.endsWith(".yml"))
			{
				loader = YAMLConfigurationLoader
						.builder()
						.setFlowStyle(FlowStyle.BLOCK)
						.setIndent(2)
						.setPath(path)
						.build();
			}
			else if(fileName.endsWith(".conf"))
			{
				loader = HoconConfigurationLoader
						.builder()
						.setPath(path)
						.build();
			}
			else if(fileName.endsWith(".json"))
			{
				loader = JSONConfigurationLoader
						.builder()
						.setPath(path)
						.build();
			}
			else if(fileName.endsWith(".xml"))
			{
				loader = XMLConfigurationLoader
						.builder()
						.setPath(path)
						.build();
			}
			else
			{
				throw new UnknownFileTypeException(fileName);
			}
		
			config.loader = loader;
			config.node = loader.load();
			return config;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		return config;
	}
	
	public static Configuration load(URL url, File tempFile, File backupFile)
	{
		return load(url, tempFile, backupFile, new HashMap<>(), true);
	}
	
	public static Configuration load(URL url, File tempFile, File backupFile, boolean overwrite)
	{
		return load(url, tempFile, backupFile, new HashMap<>(), overwrite);
	}
	
	public static Configuration load(URL url, File tempFile, File backupFile, Map<String,String> requestProperties)
	{
		return load(url, tempFile, backupFile, 10000, 10000, requestProperties, true);
	}
	
	public static Configuration load(URL url, File tempFile, File backupFile, Map<String,String> requestProperties, boolean overwrite)
	{
		return load(url, tempFile, backupFile, 10000, 10000, requestProperties, overwrite);
	}
	
	public static Configuration load(URL url, File tempFile, File backupFile, int connectionTimeout, int readTimeout, Map<String,String> requestProperties, boolean overwrite)
	{
		try 
		{
			if(backupFile != null && backupFile.exists() && backupFile.length() > 0 && !overwrite)
			{
				return Configuration.load(backupFile);
			}
			
			if(tempFile.exists())
			{
				tempFile.delete();
			}
			tempFile.createNewFile();
			
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			Iterator<Entry<String,String>> it = requestProperties.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<String,String> next = it.next();
				connection.setRequestProperty(next.getKey(), next.getValue());
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			
			String line = null;
			while((line = reader.readLine()) != null)
			{
				writer.write(line);
				writer.write("\n");
			}
			
			reader.close();
			writer.close();
			//FileUtils.copyURLToFile(url, tempFile, connectionTimeout, readTimeout);
			if(tempFile.length() > 0 && tempFile.length() != backupFile.length())
			{
				if(backupFile.exists())
				{
					backupFile.delete();
				}
				FileUtils.copyFile(tempFile, backupFile);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		if(backupFile.exists())
		{
			return Configuration.load(backupFile);
		}

		return new Configuration();
	}

	public static Configuration load(InputStream stream, ConfigurationType type)
	{
		
		ConfigurationLoader<?> loader = null;
		Configuration config = new Configuration();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		try
		{
			Callable<BufferedReader> callable = new Callable<BufferedReader>() 
			{

				@Override
				public BufferedReader call()
				{
					return reader;
				}
			};

			if(type == ConfigurationType.YAML)
			{
				loader = YAMLConfigurationLoader
						.builder()
						.setSource(callable)
						.setFlowStyle(FlowStyle.BLOCK)
						.setIndent(2)
						.build();
			}
			else if(type == ConfigurationType.HOCON)
			{
				loader = HoconConfigurationLoader
						.builder()
						.setSource(callable)
						.build();
			}
			else if(type == ConfigurationType.JSON)
			{
				loader = JSONConfigurationLoader
						.builder()
						.setSource(callable)
						.build();
			}
			else if(type == ConfigurationType.XML)
			{
				loader = XMLConfigurationLoader
						.builder()
						.setSource(callable)
						.build();
			}

			config.loader = loader;
			config.node = loader.load();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try 
			{
				reader.close();
				stream.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return config;
	}
}