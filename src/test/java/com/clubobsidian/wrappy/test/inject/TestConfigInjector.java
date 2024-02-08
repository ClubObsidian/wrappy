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
package com.clubobsidian.wrappy.test.inject;

import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.ConfigurationSection;
import com.clubobsidian.wrappy.inject.ConfigurationInjector;
import com.clubobsidian.wrappy.test.mock.ConfigHolderNonStaticMock;
import com.clubobsidian.wrappy.test.mock.ConfigHolderStaticMock;
import com.clubobsidian.wrappy.test.mock.StringNodeTransformerMock;
import com.clubobsidian.wrappy.transformer.NodeTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestConfigInjector {

    private static ConfigurationSection config;

    @BeforeEach
    public void setup() {
        config = Configuration.load(new File("test", "test-inject.yml"));
        config = config.getConfigurationSection("test");
    }

    @Test
    public void testInjectNonStatic() {
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        ConfigurationInjector injector = new ConfigurationInjector(config, mock);
        injector.inject(new ArrayList<>());
        assertEquals("bar", mock.getFoo());
    }

    @Test
    public void testInjectStatic() {
        ConfigurationInjector injector = new ConfigurationInjector(config, ConfigHolderStaticMock.class);
        injector.inject(new ArrayList<>());
        assertEquals("bar", ConfigHolderStaticMock.foo);
    }

    @Test
    public void testInjectStaticEnumList() {
        ConfigurationInjector injector = new ConfigurationInjector(config, ConfigHolderStaticMock.class);
        injector.inject(new ArrayList<>());
        assertEquals(DayOfWeek.FRIDAY, ConfigHolderStaticMock.dayList.get(0));
    }

    @Test
    public void testInjectNonStaticConfig() {
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        config.inject(mock);
        assertEquals("bar", mock.getFoo());
    }

    @Test
    public void testInjectStaticConfig() {
        config.inject(ConfigHolderStaticMock.class);
        assertEquals("bar", ConfigHolderStaticMock.foo);
    }

    @Test
    public void testInjectNonStaticConfigWithTransformer() {
        Collection<NodeTransformer> transformers = new ArrayList<>();
        transformers.add(new StringNodeTransformerMock());
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        config.inject(mock, transformers);
        assertEquals("abar", mock.getFoo());
    }


    @Test
    public void testInjectStaticConfigWithTransformer() {
        Collection<NodeTransformer> transformers = new ArrayList<>();
        transformers.add(new StringNodeTransformerMock());
        config.inject(ConfigHolderStaticMock.class, transformers);
        assertEquals("abar", ConfigHolderStaticMock.foo);
    }

    @Test
    public void testInjectorPrimitive() {
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        config.inject(mock);
        assertEquals(2, mock.getNum());
    }

    @Test
    public void testInjectorNonStringObject() {
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        config.inject(mock);
        assertEquals(UUID.fromString("8ad24b1a-963a-4b0e-b776-1befdc81f5d2"), mock.getUUID());
    }

    @Test
    public void testInjectorStringList() {
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        config.inject(mock);
        assertEquals("foobar", mock.getStrList().get(0));
    }

    @Test
    public void testInjectorEnumList() {
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        config.inject(mock);
        assertEquals(DayOfWeek.FRIDAY, mock.getDayList().get(0));
    }

    @Test
    public void testInjectorEnumMap() {
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        config.inject(mock);
        Map<DayOfWeek, Integer> dayMap = mock.getDayMap();
        assertEquals(1, dayMap.size());
        assertEquals(5, (int) dayMap.get(DayOfWeek.FRIDAY));
    }

    @Test
    public void testInjectorMockHolder() {
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        config.inject(mock);
        assertEquals(5, mock.getHolder().getNum());
    }

    @Test
    public void testInjectorKey() {
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        config.inject(mock);
        assertEquals("test", mock.getKey());
    }

    @Test
    public void testInjectorNonExistent() {
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        config.inject(mock);
        assertEquals(null, mock.getNonExistent());
    }

    @Test
    public void testInjectorNonExistentFloat() {
        ConfigHolderNonStaticMock mock = new ConfigHolderNonStaticMock();
        config.inject(mock);
        assertEquals(0.0f, mock.getNonExistentFloat(), 0.01f);
    }
}