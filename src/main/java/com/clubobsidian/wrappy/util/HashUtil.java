package com.clubobsidian.wrappy.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtil {

	private HashUtil() {}
	
	public static byte[] getMD5(File file) {
		return getHash("MD5", file);
	}
	
	public static byte[] getMD5(byte[] data) {
		return getHash("MD5", data);
	}
	
	private static byte[] getHash(String hash, File file) {
		try {
			byte[] fileBytes = Files.readAllBytes(file.toPath());
			return getHash(hash, fileBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}
	
	private static byte[] getHash(String hash, byte[] data) {
		try {
			MessageDigest md = MessageDigest.getInstance(hash);
			md.reset();
			md.update(data);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}
}