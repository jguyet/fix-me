package org.fixme.core.protocol;

public interface INetworkType {

	/**
	 * Message serializer method<br>
	 * write on buffer all message properties
	 * @param buffer
	 */
	public void serialize(ByteArrayBuffer buffer);
	
	/**
	 * Message deserializer method<br>
	 * read buffer content for load message properties
	 * @param buffer
	 */
	public void deserialize(ByteArrayBuffer buffer);
}
