package wrappy;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.ConfigurationType;

public class TestInputStream {

	@Test
	public void testLoadYaml()
	{
		try 
		{
			InputStream input = new FileInputStream(new File("test.yml"));
			Configuration config = Configuration.load(input, ConfigurationType.YAML);
			assertTrue("Yml configuration is empty", config.getKeys().size() > 0);
			input.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Test
	public void testLoadJson()
	{
		try 
		{
			InputStream input = new FileInputStream(new File("test.json"));
			Configuration config = Configuration.load(input, ConfigurationType.JSON);
			assertTrue("Json configuration is empty", config.getKeys().size() > 0);
			input.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLoadHocon()
	{
		try 
		{
			InputStream input = new FileInputStream(new File("test.hocon"));
			Configuration config = Configuration.load(input, ConfigurationType.HOCON);
			assertTrue("Hocon configuration is empty", config.getKeys().size() > 0);
			input.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
