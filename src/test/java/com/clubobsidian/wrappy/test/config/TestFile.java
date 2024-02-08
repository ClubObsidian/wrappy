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
import com.clubobsidian.wrappy.UnknownFileTypeException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFile { 

	@Test
	public void testLoadYaml() {
		Configuration config = Configuration.load(new File("test.yml"));
		assertTrue(config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.yml"));
		assertTrue(config.getKeys().size() == 0);
	}

	@Test
	public void testLoadJson() {
		Configuration config = Configuration.load(new File("test.json"));
		assertTrue(config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.json"));
		assertTrue(config.getKeys().size() == 0);
	}
	
	@Test
	public void testLoadHocon() {
		Configuration config = Configuration.load(new File("test.conf"));
		assertTrue(config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.conf"));
		assertTrue(config.getKeys().size() == 0);
	}
	
	
	@Test
	public void testLoadXml() {
		Configuration config = Configuration.load(new File("test.xml"));
		assertTrue(config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.xml"));
		assertTrue(config.getKeys().size() == 0);
	}

	@Test
	public void testUnknownFileTypeException() {
		AtomicReference<Configuration> config = new AtomicReference<>();
		assertThrows(UnknownFileTypeException.class, () ->
				config.set(Configuration.load(new File("test.jibberish")))
		);
		assertTrue(config.get() == null);
	}
	
	@Test
	public void testPath() {
		Configuration config = Configuration.load(new File("test.yml").toPath());
		assertTrue(config.getKeys().size() > 0);
	}
}