package wrappy;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.junit.Test;

import com.clubobsidian.wrappy.Configuration;

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
			Files.copy(originalFile.toPath(), copyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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