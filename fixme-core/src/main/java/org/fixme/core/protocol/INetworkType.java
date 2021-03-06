package org.fixme.core.protocol;

import org.fixme.core.utils.Json;

public interface INetworkType {

	/**
	 * Message serializer method<br>
	 * write on buffer all message properties
	 * @param buffer
	 */
	public void serialize(Json buffer);
	
	/**
	 * Message deserializer method<br>
	 * read buffer content for load message properties
	 * @param buffer
	 */
	public void deserialize(Json buffer);
}
