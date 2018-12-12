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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class ConfigurationSection {

	protected ConfigurationNode node;
	protected ConfigurationLoader<?> loader;
	
	public void save()
	{
		try 
		{
			this.loader.save(this.node);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public Object get(String path)
	{
		return this.node.getNode(this.parsePath(path)).getValue();
	}
	
	public String getString(String path)
	{
		return this.node.getNode(this.parsePath(path)).getString();
	}
	
	public int getInteger(String path)
	{
		return this.node.getNode(this.parsePath(path)).getInt();
	}
	
	public Long getLong(String path)
	{
		return this.node.getNode(this.parsePath(path)).getLong();
	}
	
	public Float getFloat(String path)
	{
		return this.node.getNode(this.parsePath(path)).getFloat();
	}
	
	public boolean getBoolean(String path)
	{
		return this.node.getNode(this.parsePath(path)).getBoolean();
	}
	
	public double getDouble(String path)
	{
		return this.node.getNode(this.parsePath(path)).getDouble();
	}
	
	public List<String> getStringList(String path)
	{
		return new NodeHelper<String>().getList(this.node.getNode(this.parsePath(path)), TypeToken.of(String.class));
	}
	
	public List<Integer> getIntegerList(String path)
	{
		return new NodeHelper<Integer>().getList(this.node.getNode(this.parsePath(path)), TypeToken.of(Integer.class));
	}
	
	public List<Long> getLongList(String path)
	{
		return new NodeHelper<Long>().getList(this.node.getNode(this.parsePath(path)), TypeToken.of(Long.class));
	}
	
	public List<Float> getFloatList(String path)
	{
		return new NodeHelper<Float>().getList(this.node.getNode(this.parsePath(path)), TypeToken.of(Float.class));
	}
	
	public List<Boolean> getBooleanList(String path)
	{
		return new NodeHelper<Boolean>().getList(this.node.getNode(this.parsePath(path)), TypeToken.of(Boolean.class));
	}
	
	public List<Double> getDoubleList(String path)
	{
		return new NodeHelper<Double>().getList(this.node.getNode(this.parsePath(path)), TypeToken.of(Double.class));
	}
	
	public ConfigurationSection createConfigurationSection(String path)
	{
		return this.getConfigurationSection(path);
	}
	
	public ConfigurationSection getConfigurationSection(String path)
	{
		ConfigurationSection section = new ConfigurationSection();
		section.node = this.node.getNode(this.parsePath(path));
		return section;
	}
	
	public void set(String path, Object toSave)
	{
		this.node.getNode(this.parsePath(path)).setValue(toSave);
	}
	
	public List<String> getKeys()
	{	
		List<String> keys = new ArrayList<String>();
		this.node.getChildrenMap().keySet().forEach(n -> keys.add((String) n));
		return keys;
	}
	
	public boolean isEmpty()
	{
		return this.getKeys().size() == 0;
	}
	
	public boolean hasKey(String key)
	{
		return this.getKeys().contains(key);
	}
	
	private Object[] parsePath(String path)
	{
		Object[] ar = new Object[1];
		if(path.contains("."))
		{
			String[] split = path.split("\\.");
			ar = new Object[split.length];
			for(int i = 0; i < split.length; i++)
			{
				ar[i] = (Object) split[i];
			}
		}
		else
		{
			ar[0] = (Object) path;
		}
		return ar;
	}
	
	private class NodeHelper<T>
	{
		public List<T> getList(ConfigurationNode node, TypeToken<T> token)
		{
			try 
			{
				return node.getList(token);
			} 
			catch (ObjectMappingException e) 
			{
				e.printStackTrace();
			}
			return null;
		}
	}
}