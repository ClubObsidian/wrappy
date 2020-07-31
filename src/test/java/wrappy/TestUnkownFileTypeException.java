/*  
   Copyright 2020 Club Obsidian and contributors.

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