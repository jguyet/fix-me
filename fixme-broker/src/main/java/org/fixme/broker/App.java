package org.fixme.broker;

import org.fixme.broker.socket.SocketBroker;
import org.fixme.core.LoggingProperties;

/**
 * Broker App
 * @author jguyet
 */
public class App 
{	
	private static SocketBroker socket;
	
	/**
	 * STATIC VARS
	 */
	public static boolean stopped = false;
	
    public static void main( String[] args )
    {
    	catchSigTerm();
    	
    	LoggingProperties.load();
    	
    	socket = new SocketBroker(BrokerProperties.SOCKET_SERVER_ROUTER_BROKER_IP_ADDRESS, BrokerProperties.SOCKET_SERVER_ROUTER_BROKER_PORT);
    	
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
