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
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestConfigurateLoading {

    private static File testFile = new File("test.yml");

    @Test
    public void loadConfigurate() {
        ConfigurationLoader<?> loader = YamlConfigurationLoader
                .builder()
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .path(testFile.toPath())
                .build();
        Configuration config = Configuration.load(loader);
        assertEquals("value", config.getString("key"));
    }
}