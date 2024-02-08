/*
 *    Copyright 2021 Club Obsidian and contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.clubobsidian.wrappy.test.config;

import com.clubobsidian.wrappy.UnknownFileTypeException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUnknownFileTypeException {

	@Test
	public void testFileConstructor() {
		UnknownFileTypeException ex = new UnknownFileTypeException(new File("test.yml"));
		assertEquals("Unknown file type for configuration file test.yml", ex.getMessage());
	}
	
	@Test
	public void testStringConstructor() {
		UnknownFileTypeException ex = new UnknownFileTypeException("test.yml");
		assertEquals("Unknown file type for configuration file test.yml", ex.getMessage());
	}
}