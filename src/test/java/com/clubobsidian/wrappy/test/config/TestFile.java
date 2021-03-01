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
package com.clubobsidian.wrappy.test.config;

import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.UnknownFileTypeException;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class TestFile { 

	@Test
	public void testLoadYaml() {
		Configuration config = Configuration.load(new File("test.yml"));
		assertTrue("Yml configuration is empty", config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.yml"));
		assertTrue("Empty yaml configuration is not empty", config.getKeys().size() == 0);
	}

	@Test
	public void testLoadJson() {
		Configuration config = Configuration.load(new File("test.json"));
		assertTrue("Json configuration is empty", config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.json"));
		assertTrue("Empty json configuration is not empty", config.getKeys().size() == 0);
	}
	
	@Test
	public void testLoadHocon() {
		Configuration config = Configuration.load(new File("test.conf"));
		assertTrue("Hocon configuration is empty", config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.conf"));
		assertTrue("Empty hocon configuration is not empty", config.getKeys().size() == 0);
	}
	
	
	@Test
	public void testLoadXml() {
		Configuration config = Configuration.load(new File("test.xml"));
		assertTrue("Xml configuration is empty", config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.xml"));
		assertTrue("Empty xml configuration is not empty", config.getKeys().size() == 0);
	}
	
	@Test(expected = UnknownFileTypeException.class)
	public void testUnknownFileTypeException() {
		Configuration config = Configuration.load(new File("test.jibberish"));
		assertTrue("Configuration was not empty for a non-existent file type", config.getKeys().size() == 0);
	}
	
	@Test
	public void testPath() {
		Configuration config = Configuration.load(new File("test.yml").toPath());
		assertTrue("Path config is empty", config.getKeys().size() > 0);
	}
}