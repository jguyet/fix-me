package org.fixme.broker;

import java.util.HashMap;
import java.util.Map;

import org.fixme.broker.prompt.BrokerPrompt;
import org.fixme.broker.prompt.CallBackRequestMessage;
import org.fixme.broker.socket.SocketBroker;
import org.fixme.core.LoggingProperties;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.types.MarketObject;

/**
 * Broker App
 * @author jguyet
 */
public class Broker 
{	
	//##############################
	//@STATICS SECTION ------------>
	//##############################
	
	public static SocketBroker				socket;
	public static SocketChannel				router = null;
	public static boolean					stopped = false;
	public static BrokerPrompt				prompt = null;
	public static Map<String, MarketObject>	markets = new HashMap<String, MarketObject>();
	public static CallBackRequestMessage	callback = null;
	
	public static boolean			initialized_connection = false;
	
	//##############################
	//@MAIN SECTION --------------->
	//##############################
	
    public static void main( String[] args )
    {
    	catchSigTerm();
    	
    	LoggingProperties.load();
    	
    	prompt = new BrokerPrompt();
    	
    	socket = new SocketBroker(BrokerProperties.SOCKET_SERVER_ROUTER_BROKER_IP_ADDRESS, BrokerProperties.SOCKET_SERVER_ROUTER_BROKER_PORT);
    	
    	socket.intialize();
    	
    	try { Thread.sleep(1000); } catch(Exception e) { e.printStackTrace(); }
    	//################
    	//WAIT STOPPED SIG
    	while (stopped == false) {
    		if (initialized_connection == true) {
    			prompt.start();
    		}
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
            		if (Broker.stopped)
            			return ;
            		System.out.println("");
            		if (socket != null) {
            			socket.stop();
            		}
                }
            }); 
    }
}
