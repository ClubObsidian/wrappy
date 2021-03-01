package com.clubobsidian.wrappy.test.helper;

import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.helper.NodeHelper;
import org.junit.Test;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class TestNodeHelper {

	@Test
	public void testInvalid() {
		Configuration config = Configuration.load(new File("test.yml"));
		ConfigurationNode node = config.getNode();
		NodeHelper<Double> helper = new NodeHelper<>(node);
		assertTrue(helper.get("key", Double.class) == null);
	}
	
	@Test
	public void testInvalidWithDefault() {
		Configuration config = Configuration.load(new File("test.yml"));
		ConfigurationNode node = config.getNode();
		NodeHelper<Double> helper = new NodeHelper<>(node);
		assertTrue(helper.get("key", Double.class, null) == null);
	}
}