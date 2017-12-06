package org.fixme.market;

import org.fixme.core.LoggingProperties;
import org.fixme.market.socket.SocketMarket;

/**
 * Hello world!
 *
 */
public class App 
{
	private static SocketMarket socket;
	
	/**
	 * STATIC VARS
	 */
	public static boolean stopped = false;
	
    public static void main( String[] args )
    {
    	catchSigTerm();
    	
    	LoggingProperties.load();
    	
    	socket = new SocketMarket(MarketProperties.SOCKET_SERVER_ROUTER_MARKET_IP_ADDRESS, MarketProperties.SOCKET_SERVER_ROUTER_MARKET_PORT);
    	
    	socket.intialize();
    	
    	//################################################################################################
    	//WAIT STOPPED SIG
    	while (stopped == false) {
    		try { Thread.sleep(500); } catch(Exception e) { e.printStackTrace(); }
    	}
    }
    
    private static void catchSigTerm() {
    	Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
                public void run() {
            		App.stopped = true;
                }   
            }); 
    }
}
