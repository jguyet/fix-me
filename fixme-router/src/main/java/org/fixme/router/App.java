package org.fixme.router;

import org.fixme.core.FixmeSocketServerChannelManager;
import org.fixme.router.servers.BrokerServerHandler;
import org.fixme.router.servers.MarketServerHandler;

/**
 * Main application.
 * @author jguyet
 */
public class App 
{	
	/**
	 * HANDLERS
	 */
	public static BrokerServerHandler	brokerHandler;
	public static MarketServerHandler	marketHandler;
	
	/**
	 * SERVERS
	 */
	public static FixmeSocketServerChannelManager	brokerServer;
	public static FixmeSocketServerChannelManager	marketServer;
	
	/**
	 * STATIC VARS
	 */
	public static boolean stopped = false;
	
    public static void main( String[] args )
    {
    	catchSigTerm();
    	
    	//################################################################################################
    	//BUILD HANDLERS
    	createHandlers();
    	//################################################################################################
    	//BUILD SERVERS
    	buildServers();
    	//################################################################################################
    	//INITIALIZE SERVERS
    	initializeServers();
    	//################################################################################################
    	//RUN SERVERS
    	runServers();
    	//################################################################################################
    	//WAIT STOPPED SIG
    	while (stopped == false) {
    		try { Thread.sleep(500); } catch(Exception e) { e.printStackTrace(); }
    	}
    	//################################################################################################
    	//STOP SERVERS
    	stopServers();
    }
    
    private static void createHandlers() {
    	brokerHandler = new BrokerServerHandler();
    	marketHandler = new MarketServerHandler();
    }
    
    private static void buildServers() {
    	brokerServer = new FixmeSocketServerChannelManager(RouterProperties.BROKER_ASYNCHRONOUS_SERVER_CHANNEL_PORT, brokerHandler, RouterProperties.MODULE_NAME);
    	marketServer = new FixmeSocketServerChannelManager(RouterProperties.MARKET_ASYNCHRONOUS_SERVER_CHANNEL_PORT, marketHandler, RouterProperties.MODULE_NAME);
    }
    
    private static void initializeServers() {
    	brokerServer.initialize();
    	marketServer.initialize();
    }
    
    private static void runServers() {
    	brokerServer.startOnAcceptSocketChannel();
    	marketServer.startOnAcceptSocketChannel();
    }
    
    private static void stopServers() {
    	brokerServer.stopSocketServerChannel();
    	marketServer.stopSocketServerChannel();
    }
    
    private static void catchSigTerm() {
    	Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
                public void run() {
            		App.stopped = true;
                    System.out.println("Inside Add Shutdown Hook");
                }   
            }); 
    }
}
