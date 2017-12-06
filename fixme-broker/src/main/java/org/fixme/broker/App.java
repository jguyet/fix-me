package org.fixme.broker;

import org.fixme.broker.socket.SocketBroker;
import org.fixme.core.LoggingProperties;

/**
 * Broker App
 * @author jguyet
 */
public class App 
{	
	//##############################
	//@STATICS SECTION ------------>
	//##############################
	
	public static SocketBroker		socket;
	public static boolean			stopped = false;
	
	//##############################
	//@MAIN SECTION --------------->
	//##############################
	
    public static void main( String[] args )
    {
    	catchSigTerm();
    	
    	LoggingProperties.load();
    	
    	socket = new SocketBroker(BrokerProperties.SOCKET_SERVER_ROUTER_BROKER_IP_ADDRESS, BrokerProperties.SOCKET_SERVER_ROUTER_BROKER_PORT);
    	
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
