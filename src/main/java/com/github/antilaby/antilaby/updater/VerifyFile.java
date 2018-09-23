package com.github.antilaby.antilaby.updater;

import sun.misc.BASE64Encoder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * Verify that a file has been downloaded correctly
 *
 * @author NathanNr
 */
public class VerifyFile {

	private final File file;
	private final String sha256;

	public VerifyFile(File file, String sha256) {
		this.file = file;
		this.sha256 = sha256;
	}

	/**
	 * Compare the hash values
	 *
	 * @return true if the hash values are equal
	 */
	public boolean verify() {
		// Compare hash
		if (!sha256.equals(getSha256(file)))
			return false;
		return true;
	}

	/**
	 * Get the SHA-256 value of a file
	 *
	 * @param file File
	 * @return SHA-256
	 */
	private String getSha256(File file) {
		try {
			byte[] buffer = new byte[8192];
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			int i;
			while ((i = bis.read(buffer)) > 0) {
				digest.update(buffer, 0, i);
			}
			bis.close();
			byte[] hash = digest.digest();
			System.out.println(new BASE64Encoder().encode(hash));
			return new BASE64Encoder().encode(hash);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
