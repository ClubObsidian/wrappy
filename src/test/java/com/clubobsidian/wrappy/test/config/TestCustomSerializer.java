package com.clubobsidian.wrappy.test.config;

import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.test.mock.FooBarMock;
import com.clubobsidian.wrappy.test.mock.FooBarSerializerMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCustomSerializer {

    private File testFolder;

    @BeforeEach
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

    @Test
    public void testCustomSerializerList() {
        File file = new File(this.testFolder, "test-serializer.yml");
        Configuration config = new Configuration.Builder()
                .file(file)
                .serializer(FooBarMock.class, new FooBarSerializerMock())
                .build();
        List<FooBarMock> fooList = config.getList("foobarlist", FooBarMock.class);
        assertEquals(2, fooList.size());
        assertEquals("test1", fooList.get(0).getFoobar());
        assertEquals("test2", fooList.get(1).getFoobar());
    }
}
