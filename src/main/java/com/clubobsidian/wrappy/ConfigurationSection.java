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

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import com.clubobsidian.wrappy.helper.NodeHelper;
import com.clubobsidian.wrappy.util.NodeUtil;
import org.spongepowered.configurate.serialize.SerializationException;

public class ConfigurationSection {

	protected ConfigurationNode node;
	protected ConfigurationLoader<?> loader;
	
	public boolean save() {
		if(!this.loader.canSave()) {
			return false;
		}
		
		try {
			this.loader.save(this.node);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
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

	public Object get(String path) {
		return this.get(path, Object.class);
	}
	
	public <T> T get(String path, Class<T> clazz) {
		return this.get(path, clazz, null);
	}
	
	public <T> T get(String path, Class<T> clazz, T defaultValue) {
		if(defaultValue == null) {
			return new NodeHelper<T>(this.node).get(path, clazz);
		}
		return new NodeHelper<T>(this.node).get(path, clazz, defaultValue);
	}
	
	public String getString(String path) {
		return this.get(path, String.class);
	}
	
	public int getInteger(String path) {
		return this.get(path, int.class, 0);
	}
	
	public long getLong(String path) {
		return this.get(path, long.class, 0L);
	}
	
	public float getFloat(String path) {
		return this.get(path, float.class, 0f);
	}
	
	public boolean getBoolean(String path) {
		return this.get(path, boolean.class, false);
	}
	
	public double getDouble(String path) {
		return this.get(path, double.class, 0.0);
	}
	
	public URI getURI(String path) {
		return this.get(path, URI.class);
	}
	
	public URL getURL(String path) {
		return this.get(path, URL.class);
	}
	
	public UUID getUUID(String path) {
		return this.get(path, UUID.class);
	}
	
	public Pattern getPattern(String path) {
		return this.get(path, Pattern.class);
	}
	
	public List<String> getStringList(String path) {
		return this.getList(path, String.class);
	}
	
	public List<Integer> getIntegerList(String path) {
		return this.getList(path, Integer.class);
	}
	
	public List<Long> getLongList(String path) {
		return this.getList(path, Long.class);
	}
	
	public List<Float> getFloatList(String path) {
		return this.getList(path, Float.class);
	}
	
	public List<Boolean> getBooleanList(String path) {
		return this.getList(path, Boolean.class);
	}
	
	public List<Double> getDoubleList(String path) {
		return this.getList(path, Double.class);
	}
	
	public List<URI> getURIList(String path) {
		return this.getList(path, URI.class);
	}
	
	public List<URL> getURLList(String path) {
		return this.getList(path, URL.class);
	}
	
	public List<UUID> getUUIDList(String path) {
		return this.getList(path, UUID.class);
	}
	
	public List<Pattern> getPatternList(String path) {
		return this.getList(path, Pattern.class);
	}
	
	public <T> List<T> getList(String path, Class<T> clazz) {
		return new NodeHelper<T>(this.node).getList(path, clazz);
	}
	
	public ConfigurationSection createConfigurationSection(String path) {
		return this.getConfigurationSection(path);
	}
	
	public ConfigurationSection getConfigurationSection(String path) {
		ConfigurationSection section = new ConfigurationSection();
		section.node = NodeUtil.parsePath(this.node, path);
		return section;
	}
	
	public boolean exists(String path) {
		return !NodeUtil.parsePath(this.node, path).virtual();
	}
	
	public void set(String path, Object toSave) {
		Object saveToPath = toSave;
		if(saveToPath instanceof List) {
			saveToPath = this.convertList(saveToPath);
		} else if(this.isSpecial(saveToPath)) {
			saveToPath = saveToPath.toString();
		}
		try {
			NodeUtil.parsePath(this.node, path).set(saveToPath);
		} catch (SerializationException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getKeys() {	
		List<String> keys = new ArrayList<>();
		this.node.childrenMap().keySet().forEach(n -> keys.add((String) n));
		return keys;
	}
	
	public boolean isEmpty() {
		return this.getKeys().size() == 0;
	}
	
	public boolean hasKey(String key) {
		return this.getKeys().contains(key);
	}
	
	private List<?> convertList(Object obj) {
		List<?> convertList = (List<?>) obj;
		if(convertList.size() == 0 || !this.isSpecial(convertList.get(0))) {
			return convertList;
		}
		List<String> newList = new ArrayList<>();
		if(convertList.size() > 0) {
			for(Object o : convertList) {
				newList.add(o.toString());
			}
		}
		return newList;
	}
	
	private boolean isSpecial(Object obj) {
		if(obj instanceof URI) {
			return true;
		} else if(obj instanceof URL) {
			return true;
		} else if(obj instanceof UUID) {
			return true;
		} else if(obj instanceof Pattern) {
			return true;
		}
		return false;
	}
}