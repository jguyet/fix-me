package org.fixme.broker;

import ch.qos.logback.classic.Level;

public class BrokerProperties {
	
	/**
	 * MODULE
	 */
	public static final String MODULE_NAME = "Broker";
	
	/**
	 * ROUTER BROKER INET ADDRESS
	 */
	public static final String	SOCKET_SERVER_ROUTER_BROKER_IP_ADDRESS = "127.0.0.1";
	
	public static final int		SOCKET_SERVER_ROUTER_BROKER_PORT = 5000;
	
	/**
	 * LOGGING
	 */
	public static final Level	HIBERNATE_LOGS_LEVEL = Level.OFF;
	
	public static final Level	REFLECTION_LOGS_LEVEL = Level.OFF;
	
	public static final Level	VALIDATOR_LOGS_LEVEL = Level.OFF;
	
	public static final Level	LOGS_LEVEL = Level.ALL;
	
}
