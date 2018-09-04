package com.clubobsidian.wrappy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;

import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.json.JSONConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

public class Configuration extends ConfigurationSection {

	public static Configuration load(File file)
	{
		Configuration config = new Configuration();
		try
		{
			String name = file.getName().toLowerCase();
			ConfigurationLoader<?> loader = null;
			
			if(name.endsWith(".yml") || name.endsWith(".yaml"))
			{
				loader = YAMLConfigurationLoader.builder().setFile(file).build();
			}
			else if(name.endsWith(".hocon"))
			{
				loader = HoconConfigurationLoader.builder().setFile(file).build();
			}
			else if(name.endsWith(".json"))
			{
				loader = JSONConfigurationLoader.builder().setFile(file).build();
			}
			else
			{
				throw new UnknownFileTypeException(file);
			}
			
			config.loader = loader;
			config.node = loader.load();
			return config;
		}
		catch(IOException | UnknownFileTypeException ex)
		{
			ex.printStackTrace();
		}
		return config;
	}
	
	public static Configuration load(Path path)
	{
		return load(path.toFile());
	}

	public static Configuration load(URL url, File tempFile, File backupFile)
	{
		try 
		{
			FileUtils.copyURLToFile(url, tempFile, 10000, 10000);
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

		return null;
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