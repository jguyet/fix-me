package org.fixme.market;

public class MarketProperties {

	/**
	 * MODULE
	 */
	public static final String MODULE_NAME = "Market";
	
	/**
	 * ROUTER MARKET INET ADDRESS
	 */
	public static final String	SOCKET_SERVER_ROUTER_MARKET_IP_ADDRESS = "127.0.0.1";
	
	public static final int		SOCKET_SERVER_ROUTER_MARKET_PORT = 5001;
	
	/**
	 * MONGO_DB HOSTNAME
	 * mlab.com
	 * mongodb://<dbuser>:<dbpassword>@ds115740.mlab.com:15740/market001
	 */
	public static final String	DATABASE_MONGO_DB_HOSTNAME = "ds115740.mlab.com";
	public static final int		DATABASE_MONGO_DB_PORT = 15740;
	/**
	 * MONGO_DB CREDENTIALS
	 */
	public static final String	DATABASE_MONGO_DB_CREDENTIAL_USER_NAME = "root";
	public static final String	DATABASE_MONGO_DB_CREDENTIAL_PASSWORD = "123";
	public static final String	DATABASE_MONGO_DB = "market001";
	
	/**
	 * TODO create script for get http://www.priceminister.com/nav/Musique_Instruments-de-Musique
	 */
}
