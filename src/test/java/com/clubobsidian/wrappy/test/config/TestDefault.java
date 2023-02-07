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
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestDefault {

    private static File testFile = new File("test.yml");
    private static Configuration config = Configuration.load(testFile);

    @Test
    public void testNodeNotNull() {
        assertTrue("Configurate node was null", config.getNode() != null);
    }

    @Test
    public void testFloat() {
        assertEquals(0.0f, config.getFloat("does-not-exist"), 0.01);
    }

}
