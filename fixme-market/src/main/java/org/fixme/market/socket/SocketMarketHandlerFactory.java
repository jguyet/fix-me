package org.fixme.market.socket;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.fixme.market.socket.reflection.MarketClassMessageHandler;
import org.fixme.market.socket.reflection.MarketMethodMessageHandler;
import org.reflections.Reflections;

public class SocketMarketHandlerFactory {

	public static Map<Integer, Method> messagesHandler = new HashMap<Integer, Method>();

	/**
	 * load all method on directory org.fixme.market.socket.handler with MarketMethodMessageHandler anotation
	 */
	static//load once
	{
		Reflections reflections = new Reflections("org.fixme.market.socket.handler");

		Set<Class<?>> allclass = reflections.getTypesAnnotatedWith(MarketClassMessageHandler.class);
		
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
					if (!(a instanceof MarketMethodMessageHandler))
						continue ;
					messagesHandler.put(((MarketMethodMessageHandler)a).value(), m);
				}
			}
		}
	}
	
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
