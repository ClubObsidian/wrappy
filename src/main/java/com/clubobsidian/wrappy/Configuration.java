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
			
			if(name.endsWith(".yml"))
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
		catch(IOException ex)
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
