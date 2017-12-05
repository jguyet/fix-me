package org.fixme.broker;

import org.fixme.broker.socket.SocketBroker;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
	public static final String	MODULE_NAME = "Broker";
	
	private static SocketBroker socket;
	
	/**
	 * STATIC VARS
	 */
	public static boolean stopped = false;
	
	static {
		/**
		 * SET LEVEL LOGS org.hibernate
		 */
		Logger log = (Logger) LoggerFactory.getLogger("org.hibernate.validator.util.Version");
		log.setLevel(BrokerProperties.HIBERNATE_LOGS_LEVEL);
		
		log = (Logger) LoggerFactory.getLogger("org.hibernate.validator.engine.resolver.DefaultTraversableResolver");
		log.setLevel(BrokerProperties.HIBERNATE_LOGS_LEVEL);
		
		log = (Logger) LoggerFactory.getLogger("org.hibernate.validator.xml.ValidationXmlParser");
		log.setLevel(BrokerProperties.HIBERNATE_LOGS_LEVEL);
		
		log = (Logger) LoggerFactory.getLogger("org.hibernate");
		log.setLevel(BrokerProperties.HIBERNATE_LOGS_LEVEL);
		
		/**
		 * SET LEVEL LOGS org.reflections
		 */
		log = (Logger) LoggerFactory.getLogger("org.reflections.Reflections");
		log.setLevel(BrokerProperties.REFLECTION_LOGS_LEVEL);
		
		/**
		 * SET LEVEL LOGS Validator
		 */
		log = (Logger) LoggerFactory.getLogger("org.fixme");
		log.setLevel(BrokerProperties.LOGS_LEVEL);
		
		log = (Logger) LoggerFactory.getLogger("org.fixme.core.validation.Validator");
		log.setLevel(BrokerProperties.VALIDATOR_LOGS_LEVEL);
	}
	
    public static void main( String[] args )
    {
    	catchSigTerm();
    	
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
