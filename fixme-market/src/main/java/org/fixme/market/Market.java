package org.fixme.market;

import org.fixme.core.LoggingProperties;
import org.fixme.core.database.Database;
import org.fixme.core.protocol.types.WalletObject;
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
	
	public static SocketMarket		socket;
	public static boolean			stopped = false;
	
	public static Database			database;
	
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
    	
//    	WalletObject wallet = database.getWalletCollection().findOne("wallet", "TEST");
//    	
//    	System.out.println(wallet.toString());
    	
    	//database.getWalletCollection().save(new WalletObject("TEST", "BTC", 10));
    	
    	//Market.database.getMarketCollection().findOne("", value)
    	
    	//QueryResults<MarketObject> a = database.getMarketCollection().find();
    	
//    	database.getMarketCollection().save(new MarketObject("ETH"));
//    	database.getMarketCollection().save(new MarketObject("BTC"));
//    	database.getMarketCollection().save(new MarketObject("XRP"));
//    	database.getMarketCollection().save(new MarketObject("EOS"));
//    	database.getMarketCollection().save(new MarketObject("LTC"));
//    	database.getMarketCollection().save(new MarketObject("NEO"));
    	
//    	for (MarketObject m : a.asList()) {
//    		System.out.println(m.getName());
//    		//System.out.println(m.getPurchase_list().toString());
//    		for (ObjectId id : m.getAsks()) {
//    			InstrumentObject o = database.getInstrumentCollection().get(id);
//    			System.out.println("ID (" + id + ") :\n" + o.getWallet() + " " + o.getPrice() + " " + o.getQuantity());
//    		}
////    		InstrumentObject i = new InstrumentObject("hh1", 15, 98);
////    		database.getInstrumentCollection().save(i);
////    		m.add_purchase_Instrument(i);
////    		database.getMarketCollection().save(m);
//    	}
    	
    	//SOCKET
    	socket = new SocketMarket(MarketProperties.SOCKET_SERVER_ROUTER_MARKET_IP_ADDRESS, MarketProperties.SOCKET_SERVER_ROUTER_MARKET_PORT);
    	
    	socket.intialize();
    	
    	//################
    	//WAIT STOPPED SIG
    	while (stopped == false) {
    		try { Thread.sleep(500); } catch(Exception e) { e.printStackTrace(); }
    	}
    	
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
