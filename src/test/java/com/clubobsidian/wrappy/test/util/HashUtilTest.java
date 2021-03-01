package com.clubobsidian.wrappy.test.util;

import com.clubobsidian.wrappy.util.HashUtil;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class HashUtilTest {

	@Test
	public void getInvalidHash() {
		try {
			Method method = HashUtil.class.getDeclaredMethod("getHash", String.class, byte[].class);
			method.setAccessible(true);
			byte[] invoked = (byte[]) method.invoke(null, "INVALID", String.valueOf("test").getBytes());
			assertTrue(invoked.length == 0);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}