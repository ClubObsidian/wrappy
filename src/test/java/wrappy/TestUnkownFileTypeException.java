package wrappy;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.clubobsidian.wrappy.UnknownFileTypeException;

public class TestUnkownFileTypeException {

	@Test
	public void testFileConstructor()
	{
		UnknownFileTypeException ex = new UnknownFileTypeException(new File("test.yml"));
		assertTrue("Message is not \"Unknown file type for configuration file test.yml\"", 
		ex.getMessage().equals("Unknown file type for configuration file test.yml"));
	}
	
	@Test
	public void testStringConstructor()
	{
		UnknownFileTypeException ex = new UnknownFileTypeException("test.yml");
		assertTrue("Message is not \"Unknown file type for configuration file test.yml\"", 
		ex.getMessage().equals("Unknown file type for configuration file test.yml"));
	}
}