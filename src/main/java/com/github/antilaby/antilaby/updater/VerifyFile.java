package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.log.Logger;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Verify that a file has been downloaded correctly by comparing the hash value
 *
 * @author NathanNr
 */
public class VerifyFile {

	private Logger logger = new Logger("VerifyFile");

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
	public boolean verify() throws IOException, NoSuchAlgorithmException {
		// Compare hash
		String fileHash = getSha256(file);
		if (!fileHash.equals(sha256)) {
			logger.debug("Hash values are not equal: " + fileHash + " !equals " + sha256);
			return false;
		}
		return true;
	}

	/**
	 * Get the SHA-256 value of a file
	 *
	 * @param file File
	 * @return SHA-256
	 */
	private String getSha256(File file) throws IOException, NoSuchAlgorithmException {
		byte[] buffer = new byte[8192];
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		int i;
		while ((i = bis.read(buffer)) > 0) {
			digest.update(buffer, 0, i);
		}
		bis.close();
		byte[] hash = digest.digest();
		return new BASE64Encoder().encode(hash);
	}

}
