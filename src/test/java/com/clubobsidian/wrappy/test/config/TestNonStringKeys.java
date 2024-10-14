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
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestNonStringKeys {

    private static File testFile = new File("test", "test-non-string-keys.yml");
    private static Configuration config = Configuration.load(testFile);

    @Test
    public void testIntegerKey() {
        assertEquals("bar", config.getConfigurationSection(1).getString("foo"));
    }

    @Test
    public void testGetKeys() {
        assertTrue(!config.getKeys().isEmpty());
    }

    @Test
    public void testSetKey() {
        config.set(123, 124);
        assertEquals(124, config.getInteger(123));
    }
}