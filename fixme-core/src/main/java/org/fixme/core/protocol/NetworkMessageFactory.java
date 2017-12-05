package org.fixme.core.protocol;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

public class NetworkMessageFactory {

	public static Map<Integer, Class<?>> messages = new HashMap<Integer, Class<?>>();

	/**
	 * load all class child of NetworkMessage on org.fixme.core.protocol.message with AnnotationMessageID anotation
	 */
	static//once loading
	{
		Reflections reflections = new Reflections("org.fixme.core.protocol.message");

		Set<Class<? extends NetworkMessage>> allClasses = reflections.getSubTypesOf(NetworkMessage.class);
		
		for (Class<?> c : allClasses)
		{
			Annotation[] annots = c.getAnnotations();
			
			if (annots.length < 0)
				continue ;
			for (Annotation a : annots)
			{
				if (!(a instanceof AnnotationMessageID))
					continue ;
				messages.put(((AnnotationMessageID)a).value(), c);
			}
		}
	}
	
	public static NetworkMessage createNetworkMessage(NetworkMessageHeader header, ByteBuffer buffer) {
		
		NetworkMessage result = null;
    	
    	try
		{
			if (messages.containsKey(header.getId()))
			{
				Constructor<?>[] contructors = messages.get(header.getId()).getConstructors();
				
				for (Constructor<?> c : contructors)
				{
					if (c.getGenericParameterTypes().length != 1)
						continue ;
					c.setAccessible(true);
					result = (NetworkMessage)c.newInstance(buffer);
					break ;
				}
				//TODO
			}
			else
			{
	    		return (null);
			}
		}catch (Exception e)
		{
			e.printStackTrace();
			return (null);
		}
        return (result);
	}
	
}
