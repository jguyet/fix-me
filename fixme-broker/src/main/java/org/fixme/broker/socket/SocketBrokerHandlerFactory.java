package org.fixme.broker.socket;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.fixme.broker.socket.reflection.BrokerClassMessageHandler;
import org.fixme.broker.socket.reflection.BrokerMethodMessageHandler;
import org.fixme.core.client.SocketChannel;
import org.fixme.core.protocol.NetworkMessage;
import org.reflections.Reflections;

public class SocketBrokerHandlerFactory {

	public static Map<Integer, Method> messagesHandler = new HashMap<Integer, Method>();

	/**
	 * load all method on directory org.fixme.broker.socket.handler with BrokerMethodMessageHandler anotation
	 */
	static//load once
	{
		Reflections reflections = new Reflections("org.fixme.broker.socket.handler");

		Set<Class<?>> allclass = reflections.getTypesAnnotatedWith(BrokerClassMessageHandler.class);
		
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
					if (!(a instanceof BrokerMethodMessageHandler))
						continue ;
					messagesHandler.put(((BrokerMethodMessageHandler)a).value(), m);
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
