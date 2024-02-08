package com.clubobsidian.wrappy.test.config;

import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.test.mock.FooBarMock;
import com.clubobsidian.wrappy.test.mock.FooBarSerializerMock;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.DayOfWeek;

import static org.junit.Assert.assertEquals;

public class TestCustomSerializer {

    private File testFolder;

    @Before
    public void before() {
        this.testFolder = new File("test");
        if(!testFolder.exists()) {
            testFolder.mkdirs();
        }
    }

    @Test
    public void testCustomSerializer() {
        File file = new File(this.testFolder, "test-serializer.yml");
        Configuration config = new Configuration.Builder()
                .file(file)
                .serializer(FooBarMock.class, new FooBarSerializerMock())
                .build();
        FooBarMock foo = config.get("foobar", FooBarMock.class);
        assertEquals("test", foo.getFoobar());
    }

}
