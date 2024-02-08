/*
 *    Copyright 2018-2024 virustotalop
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
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class TestConfigurationYaml {

    private static File testFile = new File("test.yml");
    private static Configuration config = Configuration.load(testFile);

    @Test
    public void testNodeNotNull() {
        assertTrue(config.getNode() != null);
    }

    @Test
    public void testLoaderNotNull() {
        assertTrue(config.getLoader() != null);
    }

    @Test
    public void testGet() {
        assertTrue(config.get("key") != null);
        assertTrue(config.get("non-existent-key") == null);
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
        assertTrue(config.getString("key") != null);
        assertTrue(config.getString("non-existent-key") == null);
    }

    @Test
    public void testGetInteger() {
        assertTrue(config.getInteger("integer") == 5);
        assertTrue(config.getInteger("non-existent-integer") == 0);
    }

    @Test
    public void testGetLong() {
        assertTrue(config.getLong("long") == 6);
        assertTrue(config.getLong("non-existent-long") == 0);
    }

    @Test
    public void testGetFloat() {
        float fl = config.getFloat("float");
        assertTrue(fl < 0.9 && fl > 0.7);
        assertTrue(config.getFloat("non-existent-float") == 0);
    }

    @Test
    public void testGetBoolean() {
        assertTrue(config.getBoolean("boolean"));
        assertFalse(config.getBoolean("non-existent-boolean"));
    }

    @Test
    public void testGetDouble() {
        double dub = config.getDouble("double");
        assertTrue(dub < 0.8 && dub > 0.6);
        assertTrue(config.getFloat("non-existent-double") == 0);
    }

    @Test
    public void testGetStringList() {
        List<String> list = config.getStringList("string-list");
        assertTrue(list.get(0).equals("asdf"));
        assertTrue(list.size() == 1);
    }

    @Test
    public void testGetIntegerList() {
        List<Integer> list = config.getIntegerList("integer-list");
        assertTrue(list.get(1) == 7);
        assertTrue(list.size() == 2);
    }

    @Test
    public void testGetLongList() {
        List<Long> list = config.getLongList("long-list");
        assertTrue(list.get(0) == 5);
        assertTrue(list.size() == 1);
    }

    @Test
    public void testGetFloatList() {
        List<Float> list = config.getFloatList("float-list");
        assertTrue(list.size() == 1);
        assertTrue(list.get(0) > 0);
    }

    @Test
    public void testGetBooleanList() {
        List<Boolean> list = config.getBooleanList("boolean-list");
        assertTrue(list.size() == 2);
        assertFalse(list.get(1));
    }

    @Test
    public void testGetDoubleList() {
        List<Double> list = config.getDoubleList("double-list");
        assertTrue(list.get(0) > 1 && list.get(0) < 2);
        assertTrue(list.size() == 1);
    }

    @Test
    public void testGenericIntegerList() {
        List<Integer> list = config.getList("integer-list", Integer.class);
        assertTrue(list.size() == 2);
        assertTrue(list.get(1) == 7);
    }

    @Test
    public void testGenericLongList() {
        List<Long> list = config.getList("long-list", Long.class);
        assertTrue(list.size() == 1);
        assertTrue(list.get(0) == 5);
    }

    @Test
    public void testGenericFloatList() {
        List<Float> list = config.getList("float-list", Float.class);
        assertTrue(list.size() == 1);
        assertTrue(list.get(0) > 0);
    }

    @Test
    public void testGenericBooleanList() {
        List<Boolean> list = config.getList("boolean-list", Boolean.class);
        assertTrue(list.size() == 2);
        assertFalse(list.get(1));
    }

    @Test
    public void testGenericDoubleList() {
        List<Double> list = config.getList("double-list", Double.class);
        assertTrue(list.size() == 1);
        assertTrue(list.get(0) > 1 && list.get(0) < 2);
    }

    @Test
    public void testGetKeys() {
        config = Configuration.load(testFile);
        List<Object> keys = config.getKeys();
        assertTrue(keys.size() > 0);
    }

    @Test
    public void testIsEmpty() {
        ConfigurationSection section = config.getConfigurationSection("some-empty-section");
        assertTrue(section.isEmpty());
    }

    @Test
    public void testIsNotEmpty() {
        assertFalse(config.isEmpty());
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
        assertTrue(config.hasKey("key"));
    }

    @Test
    public void testCreateConfigurationSection() {
        ConfigurationSection section = config.createConfigurationSection("section.does.not.exist");
        assertTrue(section.getKeys().size() == 0);
        assertTrue(section != null);
    }

    @Test
    public void testGetConfigurationSection() {
        ConfigurationSection section = config.getConfigurationSection("section");
        assertTrue(section.getKeys().size() == 2);
        assertTrue(section != null);
    }

    @Test
    public void testSet() {
        config.set("key", "newvalueset");
        assertTrue(config.getString("key").equals("newvalueset"));
        config.set("key", "value");
    }

    @Test
    public void testSave() {
        File saveFile = new File("saveconfig.yml");
        if (saveFile.exists()) {
            saveFile.delete();
        }
        Configuration saveConfig = Configuration.load(saveFile);
        saveConfig.set("key", "newvaluesave");


        saveConfig.save();
        saveConfig = Configuration.load(saveFile);

        assertTrue(saveConfig.getString("key").equals("newvaluesave"));
        saveConfig.set("key", "value");
        saveConfig.save();
    }

    @Test
    public void testParsePath() {
        assertTrue(config.get("section.value") != null);
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
        assertFalse(first.hasKey("value2"));
        assertEquals(7, second.getInteger("value"));
        assertEquals(5, second.getInteger("value2"));
        assertEquals(1, second.getInteger("map-value.test"));
        assertEquals("section2", second.getName());
    }
}