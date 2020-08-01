package com.clubobsidian.wrappy.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.clubobsidian.wrappy.Configuration;

public class TestSpecial {

	private File testFolder;
	
	@Before
	public void before() {
		this.testFolder = new File("test");
		if(!testFolder.exists()) {
			testFolder.mkdirs();
		}
	}

	@Test
	public void testURI() {
		try {
			URI uri = new URI("https://google.com");
			File uriFile = new File(this.testFolder, "testuri.yml");
			if(uriFile.exists()) {
				uriFile.delete();
			}
			uriFile.createNewFile();
			Configuration config = Configuration.load(uriFile);
			config.set("uri", uri);
			config.save();
			config = Configuration.load(uriFile);
			URI newURI = config.getURI("uri");
			assertTrue(uri.equals(newURI));
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testURL() {
		try {
			URL url = new URL("https://google.com");
			File uriFile = new File(this.testFolder, "testurl.yml");
			if(uriFile.exists()) {
				uriFile.delete();
			}
			uriFile.createNewFile();
			Configuration config = Configuration.load(uriFile);
			config.set("url", url);
			config.save();
			config = Configuration.load(uriFile);
			URL newURL = config.getURL("url");
			assertTrue(url.equals(newURL));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUUID() {
		try {
			UUID uuid = UUID.randomUUID();
			File uuidFile = new File(this.testFolder, "testuuid.yml");
			if(uuidFile.exists()) {
				uuidFile.delete();
			}
			uuidFile.createNewFile();
			Configuration config = Configuration.load(uuidFile);
			config.set("uuid", uuid);
			config.save();
			config = Configuration.load(uuidFile);
			UUID newUUID = config.getUUID("uuid");
			assertTrue(uuid.equals(newUUID));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPattern() {
		try {
			Pattern pattern = Pattern.compile("test");
			File patternFile = new File(this.testFolder, "testpattern.yml");
			if(patternFile.exists()) {
				patternFile.delete();
			}
			patternFile.createNewFile();
			Configuration config = Configuration.load(patternFile);
			config.set("pattern", pattern);
			config.save();
			config = Configuration.load(patternFile);
			Pattern newPattern = config.getPattern("pattern");
			assertTrue(pattern.pattern().equals(newPattern.pattern()));
			assertTrue(newPattern.matcher("test").find());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testURIList() {
		try {
			List<URI> list = new ArrayList<>();
			URI uri = new URI("https://google.com");
			list.add(uri);
			File uriFile = new File(this.testFolder, "testuri.yml");
			if(uriFile.exists()) {
				uriFile.delete();
			}
			uriFile.createNewFile();
			Configuration config = Configuration.load(uriFile);
			config.set("uri", list);
			config.save();
			config = Configuration.load(uriFile);
			List<URI> newURIList = config.getURIList("uri");
			assertTrue(uri.equals(newURIList.get(0)));
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testURLList() {
		try {
			List<URL> list = new ArrayList<>();
			URL url = new URL("https://google.com");
			list.add(url);
			File uriFile = new File(this.testFolder, "testurl.yml");
			if(uriFile.exists()) {
				uriFile.delete();
			}
			uriFile.createNewFile();
			Configuration config = Configuration.load(uriFile);
			config.set("url", list);
			config.save();
			config = Configuration.load(uriFile);
			List<URL> newURLList = config.getURLList("url");
			assertTrue(url.equals(newURLList.get(0)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUUIDList() {
		try {
			List<UUID> list = new ArrayList<>();
			UUID uuid = UUID.randomUUID();
			list.add(uuid);
			File uuidFile = new File(this.testFolder, "testuuid.yml");
			if(uuidFile.exists()) {
				uuidFile.delete();
			}
			uuidFile.createNewFile();
			Configuration config = Configuration.load(uuidFile);
			config.set("uuid", list);
			config.save();
			config = Configuration.load(uuidFile);
			List<UUID> newUUIDList = config.getUUIDList("uuid");
			assertTrue(uuid.equals(newUUIDList.get(0)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPatternList() {
		try {
			List<Pattern> list = new ArrayList<>();
			Pattern pattern = Pattern.compile("test");
			list.add(pattern);
			File patternFile = new File(this.testFolder, "testpattern.yml");
			if(patternFile.exists()) {
				patternFile.delete();
			}
			patternFile.createNewFile();
			Configuration config = Configuration.load(patternFile);
			config.set("pattern", pattern);
			config.save();
			config = Configuration.load(patternFile);
			List<Pattern> newPatternList = config.getPatternList("pattern");
			assertTrue(pattern.pattern().equals(newPatternList.get(0).pattern()));
			assertTrue(newPatternList.get(0).matcher("test").find());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}