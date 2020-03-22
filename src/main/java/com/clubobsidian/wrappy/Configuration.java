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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;

import org.yaml.snakeyaml.DumperOptions.FlowStyle;

import com.google.common.io.Files;

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
	
	public static Configuration load(URL url, File backupFile)
	{
		return load(url, backupFile, new HashMap<>(), true);
	}
	
	public static Configuration load(URL url, File backupFile, boolean overwrite)
	{
		return load(url, backupFile, new HashMap<>(), overwrite);
	}
	
	public static Configuration load(URL url, File backupFile, Map<String,String> requestProperties)
	{
		return load(url, backupFile, 10000, 10000, requestProperties, true);
	}
	
	public static Configuration load(URL url, File backupFile, Map<String,String> requestProperties, boolean overwrite)
	{
		return load(url, backupFile, 10000, 10000, requestProperties, overwrite);
	}
	
	public static Configuration load(URL url, File file, int connectionTimeout, int readTimeout, Map<String,String> requestProperties, boolean overwrite)
	{
		try 
		{
			if(file != null && file.exists() && file.length() > 0 && !overwrite)
			{
				return Configuration.load(file);
			}
			
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
			
			InputStream inputStream = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);
			char[] charData = new char[inputStream.available()];
			reader.read(charData);
			
			byte[] data = new String(charData).getBytes(StandardCharsets.UTF_8);
			
			byte[] tempMD5 = getMD5(data);
			byte[] backupMD5 = getMD5(file);
			if(charData.length > 0 && tempMD5 != backupMD5)
			{
				if(file.exists())
				{
					file.delete();
				}
				
				file.createNewFile();
				FileUtils.writeByteArrayToFile(file, data);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		if(file.exists())
		{
			return Configuration.load(file);
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
	
	private static byte[] getMD5(File file)
	{
		try 
		{
			byte[] fileBytes = Files.toByteArray(file);
			return getMD5(fileBytes);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return new byte[0];
	}
	
	
	
	private static byte[] getMD5(byte[] data)
	{
		try 
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(data);
			return md.digest();
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		
		return new byte[0];
	}
}