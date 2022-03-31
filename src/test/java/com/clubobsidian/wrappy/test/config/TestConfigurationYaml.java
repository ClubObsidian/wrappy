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
package com.clubobsidian.wrappy.test.config;

import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.ConfigurationSection;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class TestConfigurationYaml {

	private static File testFile = new File("test.yml");
	private static Configuration config = Configuration.load(testFile);
	
	@Test
	public void testNodeNotNull() {
		assertTrue("Configurate node was null", config.getNode() != null);
	}

	@Test
	public void testLoaderNotNull() {
		assertTrue("Configurate loader was null", config.getLoader() != null);
	}

	@Test
	public void testGet() {
		assertTrue("Config get is null", config.get("key") != null);
		assertTrue("Config get is not null", config.get("non-existent-key") == null);
	}

	@Test
	public void testGetMap() {
		Map<String, Integer> map = config.getMap("section");
		assertTrue(7 == map.get("value"));
	}

	@Test
	public void testSectionThatDoesNotExist() {
		Map<String, Integer> map = config.getMap("non-existent-section");
		assertEquals(null, map);
	}

	@Test
	public void testValueAsMap() {
		Map<String, Integer> map = config.getMap("key");
		assertEquals(null, map);
	}

	@Test
	public void testGetString() {
		assertTrue("Config getString is null", config.getString("key") != null);
		assertTrue("Config getString is not null", config.getString("non-existent-key") == null);
	}

	@Test
	public void testGetInteger() {
		assertTrue("Config getInteger is 0", config.getInteger("integer") == 5);
		assertTrue("Config getInteger is not 0", config.getInteger("non-existent-integer") == 0);
	}
	
	@Test
	public void testGetLong() {
		assertTrue("Config getLong is not 6", config.getLong("long") == 6);
		assertTrue("Config getLong is not 0", config.getLong("non-existent-long") == 0);
	}
	
	@Test
	public void testGetFloat() {
		float fl = config.getFloat("float");
		assertTrue("Config getFloat is not between 0.7 and 0.9", fl < 0.9 && fl > 0.7);
		assertTrue("Config getFloat is not 0", config.getFloat("non-existent-float") == 0);
	}
	
	@Test
	public void testGetBoolean() {
		assertTrue("Config getBoolean is not true", config.getBoolean("boolean"));
		assertFalse("Config getBoolean is not false", config.getBoolean("non-existent-boolean"));
	}
	
	@Test
	public void testGetDouble() {
		double dub = config.getDouble("double");
		assertTrue("Config getDouble is not between 0.6 and 0.8", dub < 0.8 && dub > 0.6);
		assertTrue("Config getDouble is not 0", config.getFloat("non-existent-double") == 0);
	}

	@Test
	public void testGetStringList() {
		List<String> list = config.getStringList("string-list");
		assertTrue("Config getStringList 0 index is not asdf", list.get(0).equals("asdf"));
		assertTrue("Config getStringList size is not 1", list.size() == 1);
	}
	
	@Test
	public void testGetIntegerList() {
		List<Integer> list = config.getIntegerList("integer-list");
		assertTrue("Config getIntegerList 1 index is not 7", list.get(1) == 7);
		assertTrue("Config getIntegerList size is not 2", list.size() == 2);
	}
	
	@Test
	public void testGetLongList() {
		List<Long> list = config.getLongList("long-list");
		assertTrue("Config getIntegerList 1 index is not 5", list.get(0) == 5);
		assertTrue("Config getIntegerList size is not 1", list.size() == 1);
	}
	
	@Test
	public void testGetFloatList() {
		List<Float> list = config.getFloatList("float-list");
		assertTrue("Config getFloatList 1 index is not greater than 0", list.get(0) > 0);
		assertTrue("Config getFloatList size is not 1", list.size() == 1);
	}
	
	@Test
	public void testGetBooleanList() {
		List<Boolean> list = config.getBooleanList("boolean-list");
		assertFalse("Config getBooleanList 1 index is not false", list.get(1));
		assertTrue("Config getBooleanList size is not 2", list.size() == 2);
	}
	
	@Test
	public void testGetDoubleList() {
		List<Double> list = config.getDoubleList("double-list");
		assertTrue("Config getDoubleList 1 index is not > 1 && < 2", list.get(0) > 1 && list.get(0) < 2);
		assertTrue("Config getDoubleList size is not 1", list.size() == 1);
	}
	
	@Test
	public void testGenericIntegerList() {
		List<Integer> list = config.getList("integer-list", Integer.class);
		assertTrue("Config getIntegerList 1 index is not 7", list.get(1) == 7);
		assertTrue("Config getIntegerList size is not 2", list.size() == 2);
	}
	
	@Test
	public void testGenericLongList() {
		List<Long> list = config.getList("long-list", Long.class);
		assertTrue("Config getIntegerList 1 index is not 5", list.get(0) == 5);
		assertTrue("Config getIntegerList size is not 1", list.size() == 1);
	}
	
	@Test
	public void testGenericFloatList() {
		List<Float> list = config.getList("float-list", Float.class);
		assertTrue("Config getFloatList 1 index is not greater than 0", list.get(0) > 0);
		assertTrue("Config getFloatList size is not 1", list.size() == 1);
	}
	
	@Test
	public void testGenericBooleanList() {
		List<Boolean> list = config.getList("boolean-list", Boolean.class);
		assertFalse("Config getBooleanList 1 index is not false", list.get(1));
		assertTrue("Config getBooleanList size is not 2", list.size() == 2);
	}
	
	@Test
	public void testGenericDoubleList() {
		List<Double> list = config.getList("double-list", Double.class);
		assertTrue("Config getDoubleList 1 index is not > 1 && < 2", list.get(0) > 1 && list.get(0) < 2);
		assertTrue("Config getDoubleList size is not 1", list.size() == 1);
	}

	@Test
	public void testGetKeys() {
		config = Configuration.load(testFile);
		List<String> keys = config.getKeys();
		assertTrue(keys.size() > 0);
	}
	
	@Test
	public void testIsEmpty() {
		ConfigurationSection section = config.getConfigurationSection("some-empty-section");
		assertTrue("An empty configuration section was not empty", section.isEmpty());
	}
	
	@Test
	public void testIsNotEmpty() {
		assertFalse("Config is empty", config.isEmpty());
	}
	
	@Test
	public void testDoesNotExist() {
		assertFalse(config.exists("does.not.exist"));
	}
	
	@Test
	public void testDoesExist() {
		assertTrue(config.exists("section.value"));
	}
	
	@Test
	public void testHasKey() {
		assertTrue("Config does not have the key \"key\"", config.hasKey("key"));
	}
	
	@Test
	public void testCreateConfigurationSection() {
		ConfigurationSection section = config.createConfigurationSection("section.does.not.exist");
		assertTrue("Section is null", section != null);
		assertTrue("Section key size is not 0", section.getKeys().size() == 0);
	}
	
	@Test
	public void testGetConfigurationSection() {
		ConfigurationSection section = config.getConfigurationSection("section");
		assertTrue("Section is null", section != null);
		assertTrue("Section key size is not 1", section.getKeys().size() == 1);
	}
	
	@Test
	public void testSet() {
		config.set("key", "newvalueset");
		assertTrue("Set was not able to set key to newvalueset", config.getString("key").equals("newvalueset"));
		config.set("key", "value");
	}
	
	@Test
	public void testSave() {
		File saveFile = new File("saveconfig.yml");
		if(saveFile.exists()) {
			saveFile.delete();
		}
		Configuration saveConfig = Configuration.load(saveFile);
		saveConfig.set("key", "newvaluesave");
		
		
		saveConfig.save();
		saveConfig = Configuration.load(saveFile);
		
		assertTrue("Save did not save value to key", saveConfig.getString("key").equals("newvaluesave"));
		saveConfig.set("key", "value");
		saveConfig.save();
	}
	
	@Test
	public void testParsePath() {
		assertTrue("Path could not be resolved", config.get("section.value") != null);
	}

	@Test
	public void testGetName() {
		assertEquals(config.getConfigurationSection("section").getName(), "section");
	}

	@Test
	public void testCombine() {
		ConfigurationSection first = config.getConfigurationSection("section");
		ConfigurationSection second = config.getConfigurationSection("section2");
		second.combine(first);
		assertNotEquals(5, first.getInteger("value2"));
		assertEquals(7, second.getInteger("value"));
		assertEquals(5, second.getInteger("value2"));
		assertEquals(1, second.getInteger("map-value.test"));
		assertEquals("section2", second.getName());
	}
}