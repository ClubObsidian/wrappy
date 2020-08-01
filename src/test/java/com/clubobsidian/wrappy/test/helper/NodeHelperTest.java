package com.clubobsidian.wrappy.test.helper;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.spongepowered.configurate.ConfigurationNode;

import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.helper.NodeHelper;

public class NodeHelperTest {

	@Test
	public void testInvalid() {
		Configuration config = Configuration.load(new File("test.yml"));
		ConfigurationNode node = config.getNode();
		NodeHelper<Double> helper = new NodeHelper<>(node);
		assertTrue(helper.get("key", Double.class) == null);
	}
}