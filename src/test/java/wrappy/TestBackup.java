/*  
   Copyright 2018 Club Obsidian and contributors.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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
		File copyFrom = new File(tempFolder, "temp.yml");
		File copyTo = new File("backup.yml");
		try 
		{
			Configuration config = Configuration.load(testFile.toURI().toURL(), copyFrom, copyTo);
			assertTrue("Backup configuration is empty", config.getKeys().size() > 0);
			config.set("test", 1);
			config = Configuration.load(testFile.toURI().toURL(), copyFrom, copyTo);
			assertTrue("Backup configuration is empty delete occured", config.getKeys().size() > 0);
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		//Cleanup after test
		try 
		{
			if(tempFolder.exists())
			{
				FileUtils.deleteDirectory(tempFolder);
			}

			if(copyTo.exists())
			{
				copyTo.delete();
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}	
	}
}
