package org.fixme.broker;

import org.fixme.broker.server.BrokerSocket;

/**
 * Hello world!
 *
 */
public class App 
{
	private static BrokerSocket socket;
	
	/**
	 * STATIC VARS
	 */
	public static boolean stopped = false;
	
    public static void main( String[] args )
    {
    	catchSigTerm();
    	
    	socket = new BrokerSocket();
    	
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
                    System.out.println("Inside Add Shutdown Hook");
                }   
            }); 
    }
}
