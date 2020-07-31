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
package com.clubobsidian.wrappy.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.junit.Test;

import com.clubobsidian.wrappy.Configuration;

public class TestInvalidLoading {

	@Test
	public void testLoadingIOException() {
		Configuration config = Configuration.load(new File("test-invalid.yml"));
		assertTrue("Node exists after throwing IOException", config.getNode() == null);
	}
	
	@Test
	public void testSaving() {
		File originalFile = new File("test-invalid.yml");
		File copyFile = new File("test-invalid-copy.yml");
		try {
			Files.copy(originalFile.toPath(), copyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Configuration config = Configuration.load(copyFile);
		config.save();
		assertTrue("Node exists after throwing IOException", config.getNode() == null);
		copyFile.delete();
	}
}