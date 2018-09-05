package wrappy;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.UnknownFileTypeException;

public class TestFile { 

	
	@Test
	public void testLoadYaml()
	{
		Configuration config = Configuration.load(new File("test.yml"));
		assertTrue("Yml configuration is empty", config.getKeys().size() > 0);
		config = Configuration.load(new File("test.yaml"));
		assertTrue("Yaml configuration is empty", config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.yml"));
		assertTrue("Empty yaml configuration is not empty", config.getKeys().size() == 0);
	}

	@Test
	public void testLoadJson()
	{
		Configuration config = Configuration.load(new File("test.json"));
		assertTrue("Json configuration is empty", config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.json"));
		assertTrue("Empty json configuration is not empty", config.getKeys().size() == 0);
	}
	
	@Test
	public void testLoadHocon()
	{
		Configuration config = Configuration.load(new File("test.hocon"));
		assertTrue("Hocon configuration is empty", config.getKeys().size() > 0);
		config = Configuration.load(new File("doesnotexist.hocon"));
		assertTrue("Empty hocon configuration is not empty", config.getKeys().size() == 0);
	}
	
	@Test(expected = UnknownFileTypeException.class)
	public void testUnknownFileTypeException()
	{
		Configuration.load(new File("test.jibberish"));
	}
	
	@Test
	public void testPath()
	{
		Configuration config = Configuration.load(new File("test.yml").toPath());
		assertTrue("Path config is empty", config.getKeys().size() > 0);
	}
}
