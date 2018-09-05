package wrappy;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.clubobsidian.wrappy.Configuration;

public class TestBackup {

	@Test
	public void setBackupLoad()
	{
		File testFile = new File("test.yml");
		File tempFolder = new File("temp");
		if(tempFolder.exists())
		{
			tempFolder.delete();
		}
		tempFolder.mkdir();
		File tempFile = new File(tempFolder, "temp.yml");
		File backupFile = new File("backup.yml");
		try 
		{
			Configuration config = Configuration.load(testFile.toURI().toURL(), tempFile, backupFile);
			assertTrue("Backup configuration is empty", config.getKeys().size() > 0);
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		try 
		{
			if(tempFolder.exists())
			{
				FileUtils.deleteDirectory(tempFolder);
			}
			
			if(backupFile.exists())
			{
				backupFile.delete();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
}