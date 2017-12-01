package org.fixme.router;

/**
 * Main application.
 * @author jguyet
 */
public class App 
{	
	public static BrokerServerHandler brokerHandler;
	public static MarketServerHandler marketHandler;
	
	private static boolean stopped = false;
	
    public static void main( String[] args )
    {
    	catchSigTerm();
    	
    	//################################################################################################
    	//BUILD HANDLERS
    	buildHandlers();
    	//################################################################################################
    	//START HANDLERS
    	runHandlers();
    	//################################################################################################
    	//WAIT STOPPED SIG
    	while (stopped == false) {
    		try { Thread.sleep(500); } catch(Exception e) { e.printStackTrace(); }
    	}
    	//################################################################################################
    	//STOP HANDLERS
    	stopHandlers();
    }
    
    private static void buildHandlers() {
    	brokerHandler = new BrokerServerHandler(RouterProperties.BROKER_ASYNCHRONOUS_SERVER_CHANNEL_PORT);
    	marketHandler = new MarketServerHandler(RouterProperties.MARKET_ASYNCHRONOUS_SERVER_CHANNEL_PORT);
    }
    
    private static void runHandlers() {
    	brokerHandler.run();
    	marketHandler.run();
    }
    
    private static void stopHandlers() {
    	brokerHandler.stop();
    	marketHandler.stop();
    }
    
    private static void catchSigTerm() {
    	Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
                public void run() {
                    System.out.println("Inside Add Shutdown Hook");
                }   
            }); 
    }
}
