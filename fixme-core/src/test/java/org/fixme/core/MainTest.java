package org.fixme.core;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainTest {

	private static Logger logger = LoggerFactory.getLogger(MainTest.class);
	
	public static void main(String ...args) {
		
		try {
			logger.info("InetAddress: {}", new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 5000).toString());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
