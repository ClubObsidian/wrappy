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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class TestURLConfiguration {

	@Test
	public void testUrlLoading() {
		File testFile = new File("test.yml");
		File copyTo = new File("backup.yml");
		try {
			Configuration config = Configuration.load(testFile.toURI().toURL(), copyTo);
			assertTrue("Backup configuration is not empty", config.getKeys().size() > 0);
			config.set("test", 1);
			config = Configuration.load(testFile.toURI().toURL(), copyTo);
			assertTrue("Backup configuration is empty delete occured", config.getKeys().size() > 0);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		if(copyTo.exists()) {
			copyTo.delete();
		}	
	}
	
	@Test
	public void testUrlLoadingWithProperties() {
		File copyTo = new File("backup.yml");
		try {
			Map<String, String> properties = new HashMap<>();
			URL url = new URL("https://raw.githubusercontent.com/ClubObsidian/wrappy-test-files/master/test.yml");
			Configuration config = Configuration.load(url, copyTo, properties);
			assertTrue(config.getInteger("rows") == 5);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if(copyTo.exists()) {
			copyTo.delete();
		}	
	}
	
	@Test
	public void testUrlLoadingBigFile() {
		File copyTo = new File("backup.yml");
		try {
			Map<String, String> properties = new HashMap<>();
			URL url = new URL("https://raw.githubusercontent.com/ClubObsidian/wrappy-test-files/master/bigtest%2Cyml");
			Configuration config = Configuration.load(url, copyTo, properties);
			assertTrue(config.getInteger("rows") == 7);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if(copyTo.exists()) {
			copyTo.delete();
		}	
	}
	
	@Test
	public void backupTest() {
		File backupFile = new File("backup.yml");
		try {
			backupFile.createNewFile();
			Configuration config = Configuration.load(backupFile);
			config.set("key", "value");
			config.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			File testFile = new File("test.yml");
			Configuration config = Configuration.load(testFile.toURI().toURL(), backupFile, false);
			assertTrue("Config was overwritten", config.getKeys().size() == 1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		if(backupFile.exists()) {
			backupFile.delete();
		}
	}
}