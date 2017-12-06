package org.fixme.market;

import org.fixme.core.LoggingProperties;
import org.fixme.market.socket.SocketMarket;

/**
 * Market App
 * @author jguyet
 */
public class App 
{
	//##############################
	//@STATICS SECTION ------------>
	//##############################
	
	public static SocketMarket		socket;
	public static boolean			stopped = false;
	
	//##############################
	//@MAIN SECTION --------------->
	//##############################
	
    public static void main( String[] args )
    {
    	catchSigTerm();
    	
    	LoggingProperties.load();
    	
    	socket = new SocketMarket(MarketProperties.SOCKET_SERVER_ROUTER_MARKET_IP_ADDRESS, MarketProperties.SOCKET_SERVER_ROUTER_MARKET_PORT);
    	
    	socket.intialize();
    	
    	//################
    	//WAIT STOPPED SIG
    	while (stopped == false) {
    		try { Thread.sleep(500); } catch(Exception e) { e.printStackTrace(); }
    	}
    	
    	socket.stop();
    }

	//##############################
	//@SIGNAL SECTION ------------->
	//##############################
    
    private static void catchSigTerm() {
    	Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
                public void run() {
            		if (App.stopped)
            			return ;
            		System.out.println("Shutdown hook system exit");
        			if (socket != null) {
        				socket.stop();
        			}
                }   
            });
    }
}
