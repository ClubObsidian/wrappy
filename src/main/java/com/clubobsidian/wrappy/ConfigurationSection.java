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
import com.clubobsidian.wrappy.helper.NodeUtil;

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
	
	public Object get(String path) {
		return new NodeHelper<Object>(this.node).get(path, Object.class);
	}
	
	public String getString(String path) {
		return new NodeHelper<String>(this.node).get(path, String.class);
	}
	
	public int getInteger(String path) {
		return NodeUtil.parsePath(this.node, path).getInt();
	}
	
	public long getLong(String path) {
		return NodeUtil.parsePath(this.node, path).getLong();
	}
	
	public float getFloat(String path) {
		return NodeUtil.parsePath(this.node, path).getFloat();
	}
	
	public boolean getBoolean(String path) {
		return NodeUtil.parsePath(this.node, path).getBoolean();
	}
	
	public double getDouble(String path) {
		return NodeUtil.parsePath(this.node, path).getDouble();
	}
	
	public URI getURI(String path) {
		return new NodeHelper<URI>(this.node).get(path, URI.class);
	}
	
	public URL getURL(String path) {
		return new NodeHelper<URL>(this.node).get(path, URL.class);
	}
	
	public UUID getUUID(String path) {
		return new NodeHelper<UUID>(this.node).get(path, UUID.class);
	}
	
	public Pattern getPattern(String path) {
		return new NodeHelper<Pattern>(this.node).get(path, Pattern.class);
	}
	
	public List<String> getStringList(String path) {
		return new NodeHelper<String>(this.node).getList(path, String.class);
	}
	
	public List<Integer> getIntegerList(String path) {
		return new NodeHelper<Integer>(this.node).getList(path, Integer.class);
	}
	
	public List<Long> getLongList(String path) {
		return new NodeHelper<Long>(this.node).getList(path, Long.class);
	}
	
	public List<Float> getFloatList(String path) {
		return new NodeHelper<Float>(this.node).getList(path, Float.class);
	}
	
	public List<Boolean> getBooleanList(String path) {
		return new NodeHelper<Boolean>(this.node).getList(path, Boolean.class);
	}
	
	public List<Double> getDoubleList(String path) {
		return new NodeHelper<Double>(this.node).getList(path, Double.class);
	}
	
	public List<URI> getURIList(String path) {
		return new NodeHelper<URI>(this.node).getList(path, URI.class);
	}
	
	public List<URL> getURLList(String path) {
		return new NodeHelper<URL>(this.node).getList(path, URL.class);
	}
	
	public List<UUID> getUUIDList(String path) {
		return new NodeHelper<UUID>(this.node).getList(path, UUID.class);
	}
	
	public List<Pattern> getPatternList(String path) {
		return new NodeHelper<Pattern>(this.node).getList(path, Pattern.class);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getList(String path, Class<?> clazz) {
		return new NodeHelper(this.node).getList(path, clazz);
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
		return !NodeUtil.parsePath(this.node, path).isVirtual();
	}
	
	public void set(String path, Object toSave) {
		NodeUtil.parsePath(this.node, path).setValue(toSave);
	}
	
	public List<String> getKeys() {	
		List<String> keys = new ArrayList<String>();
		this.node.getChildrenMap().keySet().forEach(n -> keys.add((String) n));
		return keys;
	}
	
	public boolean isEmpty() {
		return this.getKeys().size() == 0;
	}
	
	public boolean hasKey(String key) {
		return this.getKeys().contains(key);
	}
}