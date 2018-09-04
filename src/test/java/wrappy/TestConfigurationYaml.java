package wrappy;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.clubobsidian.wrappy.Configuration;

public class TestConfigurationYaml {

	private static Configuration config = Configuration.load(new File("test.yml"));
	
	@Test
	public void testGet()
	{
		assertTrue("Config get is null", config.get("key") != null);
		assertTrue("Config get is not null", config.get("non-existent-key") == null);
	}
	
	@Test
	public void testGetString()
	{
		assertTrue("Config getString is null", config.getString("key") != null);
		assertTrue("Config getString is not null", config.getString("non-existent-key") == null);
	}

	@Test
	public void testGetInteger()
	{
		assertTrue("Config getInteger is 0", config.getInteger("integer") == 5);
		assertTrue("Config getInteger is not 0", config.getInteger("non-existent-integer") == 0);
	}
	
	@Test
	public void testGetLong()
	{
		assertTrue("Config getLong is not 6", config.getLong("long") == 6);
		assertTrue("Config getLong is not 0", config.getLong("non-existent-long") == 0);
	}
	
	@Test
	public void testGetFloat()
	{
		float fl = config.getFloat("float");
		assertTrue("Config getFloat is not between 0.7 and 0.9", fl < 0.9 && fl > 0.7);
		assertTrue("Config getFloat is not 0", config.getFloat("non-existent-float") == 0);
	}
	
	@Test
	public void testGetBoolean()
	{
		assertTrue("Config getBoolean is not true", config.getBoolean("boolean"));
		assertTrue("Config getBoolean is not false", config.getBoolean("non-existent-boolean") == false);
	}
	
	@Test
	public void testGetDouble()
	{
		double dub = config.getDouble("double");
		assertTrue("Config getDouble is not between 0.6 and 0.8", dub < 0.8 && dub > 0.6);
		assertTrue("Config getDouble is not 0", config.getFloat("non-existent-double") == 0);
	}
}
