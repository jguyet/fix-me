package org.fixme.market;

import org.fixme.core.LoggingProperties;
import org.fixme.core.database.Database;
import org.fixme.market.socket.SocketMarket;

/**
 * Market App
 * @author jguyet
 */
public class Market 
{
	//##############################
	//@STATICS SECTION ------------>
	//##############################
	
	public static SocketMarket			socket;
	public static boolean				stopped = false;
	
	public static Database				database;
	private static MarketOrderTasker	marketOrderTasker;
	
	//##############################
	//@MAIN SECTION --------------->
	//##############################
	
    public static void main( String[] args )
    {
    	catchSigTerm();
    	
    	LoggingProperties.load();
    	
    	//DATABASE
    	database = new Database(MarketProperties.DATABASE_MONGO_DB_HOSTNAME, MarketProperties.DATABASE_MONGO_DB_PORT, MarketProperties.DATABASE_MONGO_DB);
    	
    	database.buildCredential(MarketProperties.DATABASE_MONGO_DB_CREDENTIAL_USER_NAME, MarketProperties.DATABASE_MONGO_DB_CREDENTIAL_PASSWORD);
    	database.buildDatabaseOptions();
    	database.buildDatabaseConnection();
    	database.buildCollections();
    	
    	Seed.SeedMongoDB(database);
    	
    	//SOCKET
    	socket = new SocketMarket(MarketProperties.SOCKET_SERVER_ROUTER_MARKET_IP_ADDRESS, MarketProperties.SOCKET_SERVER_ROUTER_MARKET_PORT);
    	
    	socket.intialize();
    	
    	marketOrderTasker = new MarketOrderTasker();
    	
    	//################
    	//WAIT STOPPED SIG
    	while (stopped == false) {
    		try { Thread.sleep(500); } catch(Exception e) { e.printStackTrace(); }
    	}
    	marketOrderTasker.stop();
    	socket.stop();
    	database.closeDatabaseConnection();
    }

	//##############################
	//@SIGNAL SECTION ------------->
	//##############################
    
    private static void catchSigTerm() {
    	Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
                public void run() {
            		if (Market.stopped)
            			return ;
            		System.out.println("Shutdown hook system exit");
        			socket.stop();
        			database.closeDatabaseConnection();
                }   
            });
    }
}
