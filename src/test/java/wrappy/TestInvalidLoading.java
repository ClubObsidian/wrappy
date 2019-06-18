package wrappy;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.clubobsidian.wrappy.Configuration;
import com.google.common.io.Files;

public class TestInvalidLoading {

	@Test
	public void testLoadingIOException()
	{
		Configuration config = Configuration.load(new File("test-invalid.yml"));
		assertTrue("Node exists after throwing IOException", config.getNode() == null);
	}
	
	@Test
	public void testSaving()
	{
		File originalFile = new File("test-invalid.yml");
		File copyFile = new File("test-invalid-copy.yml");
		try 
		{
			Files.copy(originalFile, copyFile);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		Configuration config = Configuration.load(copyFile);
		config.save();
		assertTrue("Node exists after throwing IOException", config.getNode() == null);
		copyFile.delete();
	}
}