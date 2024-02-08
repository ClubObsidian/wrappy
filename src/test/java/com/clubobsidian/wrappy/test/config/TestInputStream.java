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
import com.clubobsidian.wrappy.ConfigurationType;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestInputStream {

    @Test
    public void testLoadYaml() {
        try {
            InputStream input = new FileInputStream(new File("test.yml"));
            Configuration config = Configuration.load(input, ConfigurationType.YAML);
            assertTrue(config.getKeys().size() > 0);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadJson() {
        try {
            InputStream input = new FileInputStream(new File("test.json"));
            Configuration config = Configuration.load(input, ConfigurationType.JSON);
            assertTrue(config.getKeys().size() > 0);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadHocon() {
        try {
            InputStream input = new FileInputStream(new File("test.conf"));
            Configuration config = Configuration.load(input, ConfigurationType.HOCON);
            assertTrue(config.getKeys().size() > 0);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadXml() {
        try {
            InputStream input = new FileInputStream(new File("test.xml"));
            Configuration config = Configuration.load(input, ConfigurationType.XML);
            assertTrue(config.getKeys().size() > 0);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInputStreamSaving() {
        try {
            InputStream input = new FileInputStream(new File("test.yml"));
            Configuration config = Configuration.load(input, ConfigurationType.YAML);
            assertFalse(config.save());
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}