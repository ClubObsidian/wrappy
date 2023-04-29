package com.clubobsidian.wrappy.test.config;

import com.clubobsidian.wrappy.Configuration;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class TestNonStringKeys {

    private static File testFile = new File("test", "test-non-string-keys.yml");
    private static Configuration config = Configuration.load(testFile);

    @Test
    public void testIntegerKey() {
        assertEquals("bar", config.getConfigurationSection(1).getString("foo"));
    }

    @Test
    public void testGetKeys() {
        assertTrue(config.getKeys().size() > 0);
    }

    @Test
    public void testSetKey() {
        config.set(123, 124);
        assertTrue(124 == config.getInteger(123));
    }
}