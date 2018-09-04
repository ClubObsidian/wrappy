package wrappy;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.clubobsidian.wrappy.Configuration;

public class TestFile { 
	
	@Test
	public void testLoadYaml()
	{
		Configuration config = Configuration.load(new File("test.yml"));
		assertTrue(config != null);
		assertTrue(config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.yml"));
		assertTrue(config.getKeys().size() == 0);
	}

	@Test
	public void testLoadJson()
	{
		Configuration config = Configuration.load(new File("test.json"));
		assertTrue(config != null);
		assertTrue(config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.json"));
		assertTrue(config.getKeys().size() == 0);
	}
	
	@Test
	public void testLoadHocon()
	{
		Configuration config = Configuration.load(new File("test.hocon"));
		assertTrue(config != null);
		assertTrue(config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.hocon"));
		assertTrue(config.getKeys().size() == 0);
	}
}
