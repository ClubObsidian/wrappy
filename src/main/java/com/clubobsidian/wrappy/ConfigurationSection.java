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

import com.clubobsidian.wrappy.helper.NodeHelper;
import com.clubobsidian.wrappy.inject.ConfigurationInjector;
import com.clubobsidian.wrappy.transformer.NodeTransformer;
import com.clubobsidian.wrappy.util.NodeUtil;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class ConfigurationSection {

	private static final Map<Class<?>, Object> DEFAULT_VALUES = new HashMap() {{
		put(int.class, 0);
		put(long.class, 0l);
		put(float.class, 0f);
		put(boolean.class, false);
		put(double.class, 0.0);
	}};

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
		Object def = DEFAULT_VALUES.get(clazz);
		return this.get(path, clazz, def == null ? null : (T) def);
	}
	
	public <T> T get(String path, Class<T> clazz, T defaultValue) {
		return new NodeHelper<T>(this).get(path, clazz, defaultValue);
	}

	@Deprecated
	public <K, V> Map<K, V> getMap(String path) {
		Object objMap = this.get(path);
		if(objMap == null || !(objMap instanceof Map)) {
			return null;
		}
		return (Map<K, V>) objMap;
	}

	public <K, V> Map<K, V> getMap(String path, Class<K> keyType, Class<V> valueType) {
		ConfigurationSection virtual = new ConfigurationSection();
		virtual.node = BasicConfigurationNode.factory().createNode();
		ConfigurationSection section = this.getConfigurationSection(path);
		Collection<String> keys = section.getKeys();
		if(keys.size() == 0) {
			return null;
		}
		Map<K, V> map = new HashMap<>();
		for (String key : keys) {
			V mapValue = (V) section.get(key, valueType);
			String uuid = UUID.randomUUID().toString();
			virtual.set(uuid, key);
			K mapKey = (K) virtual.get(uuid, keyType);
			map.put(mapKey, mapValue);
		}
		return map;
	}
	
	public String getString(String path) {
		return this.get(path, String.class);
	}
	
	public int getInteger(String path) {
		return this.get(path, int.class);
	}
	
	public long getLong(String path) {
		return this.get(path, long.class);
	}
	
	public float getFloat(String path) {
		return this.get(path, float.class);
	}
	
	public boolean getBoolean(String path) {
		return this.get(path, boolean.class);
	}
	
	public double getDouble(String path) {
		return this.get(path, double.class);
	}

	public <T extends Enum> T getEnum(String path, Class<T> enumClass) {
		return this.get(path, enumClass);
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

	public <T extends Enum> List<T> getEnumList(String path, Class<T> enumClass) {
		return this.getList(path, enumClass);
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
		return new NodeHelper<T>(this).getList(path, clazz);
	}
	
	public ConfigurationSection createConfigurationSection(String path) {
		return this.getConfigurationSection(path);
	}
	
	public ConfigurationSection getConfigurationSection(String path) {
		ConfigurationSection section = new ConfigurationSection();
		section.node = NodeUtil.parsePath(this.node, path);
		return section;
	}

	public ConfigurationSection combine(ConfigurationSection from) {
		this.node.mergeFrom(from.node);
		return this;
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

	public void inject(Class<?> injectInto) {
		this.inject(injectInto, new ArrayList<>());
	}

	public void inject(Class<?> injectInto, Collection<NodeTransformer> transformers) {
		this.inject((Object) injectInto, transformers);
	}

	public void inject(Object injectInto) {
		this.inject(injectInto, new ArrayList<>());
	}

	public void inject(Object injectInto, Collection<NodeTransformer> transformers) {
		ConfigurationInjector injector = new ConfigurationInjector(this, injectInto);
		injector.inject(transformers);
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
		Class<?> clazz = obj.getClass();
		return !clazz.isPrimitive() && !clazz.equals(String.class);
	}
}