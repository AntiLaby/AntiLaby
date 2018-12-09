package com.github.antilaby.antilaby.updater;

import com.github.antilaby.antilaby.log.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Verify that a file has been downloaded correctly by comparing the hash value
 *
 * @author NathanNr
 */
public class VerifyFile {

	private static final Logger logger = new Logger("VerifyFile");

	private final Path path;
	private final String sha256;

	public VerifyFile(Path path, String sha256) {
		this.path = path;
		this.sha256 = sha256;
	}

	/**
	 * Compare the hash values
	 *
	 * @return true if the hash values are equal
	 */
	public boolean validate() throws IOException, NoSuchAlgorithmException {
		String fileHash = getSha256(path);
		if (!fileHash.equals(sha256)) {
			logger.debug("Hash values are not equal: calculated '" + fileHash + "' but expected '" + sha256 + "'");
			return false;
		}
		return true;
	}

	/**
	 * Get the SHA-256 value of a file
	 *
	 * @param path File
	 * @return SHA-256
	 */
	private static String getSha256(Path path) throws IOException, NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(Files.readAllBytes(path));
		return new String(digest.digest());
	}
}
