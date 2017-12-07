package org.fixme.router.servers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.core.reflection.handler.ClassMessageHandler;
import org.fixme.core.reflection.handler.MethodMessageHandler;
import org.reflections.Reflections;

public class MarketSocketServerMessageHandlerFactory {

	//##############################
	//@STATICS REFLECTION SECTION ->
	//##############################
	
	public static Map<Integer, Method> messagesHandler = new HashMap<Integer, Method>();

	/**
	 * load all method on directory org.fixme.router.servers.market.handler with MethodMessageHandler anotation
	 */
	static//load once
	{
		Reflections reflections = new Reflections("org.fixme.router.servers.market.handler");

		Set<Class<?>> allclass = reflections.getTypesAnnotatedWith(ClassMessageHandler.class);
		
		for (Class<?> c : allclass)
		{
			Method[] allMethod = c.getDeclaredMethods();
			
			for (Method m : allMethod)
			{
				Annotation[] annots = m.getAnnotations();
				
				if (annots.length < 0)
					continue ;
				for (Annotation a : annots)
				{
					if (!(a instanceof MethodMessageHandler))
						continue ;
					messagesHandler.put(((MethodMessageHandler)a).value(), m);
				}
			}
		}
	}

	//##############################################################################
	//@FACTORY METHODS SECTION ---------------------------------------------------->
	//##############################################################################
	
	/**
	 * check if messagesHandler contains method with annotation value == message.getTypeId()<br>
	 * return result on execution method
	 * if method doesn't exist return false
	 * @param message
	 * @return
	 */
    public static boolean handleMessage(SocketChannel channel, NetworkMessage message) {

    	try
    	{   
    		if (messagesHandler.containsKey(message.messageId()))
	        {
	        	Method m = messagesHandler.get(message.messageId());
	        	
	        	m.setAccessible(true);
	        	boolean result = (Boolean) m.invoke(Boolean.class, channel, message);
	        	return (result);
	        }
    	} catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	return (false);
    }
}
