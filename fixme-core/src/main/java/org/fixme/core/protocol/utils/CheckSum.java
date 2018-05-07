package org.fixme.core.protocol.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckSum {

	/**
	 * STATIC LOGGER
	 */
	private static Logger logger = LoggerFactory.getLogger(CheckSum.class);
	
	/**
	 * Gen checksum by byte array of element
	 * @param array
	 * @return
	 */
	public static String get(byte[] array) {
		String checkSum = "";
		try {
		byte[] byteCheckSum = createChecksum(array);

		for (int i=0; i < byteCheckSum.length; i++) {
		   checkSum += Integer.toString( ( byteCheckSum[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException fail generate checkSum");
		}
		return checkSum;
	}
	
	/**
	 * Compare byte array of element by based checksum generated before
	 * @param checkSum
	 * @param array
	 * @return
	 */
	public static boolean compareCheckSumToArray(String checkSum, byte[] array) {
		String newCheckSum = CheckSum.get(array);
		
		return checkSum.equalsIgnoreCase(newCheckSum);
	}
	
	/**
	 * create checksum byte array by byte array of elements
	 * @param array
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] createChecksum(byte[] array) throws NoSuchAlgorithmException {
		MessageDigest complete = MessageDigest.getInstance("MD5");
		
		complete.update(array, 0, array.length);
		return complete.digest();
	}
}
