package org.fixme.core.protocol;

import java.nio.ByteBuffer;

public class NetworkProtocolMessage {
	
	/**
	 * SIZE OF HEADER = INT32 + INT32
	 */
	public static final int STATIC_HEADER_LEN = 16;
	
	/**
	 * read message Header<br>
	 * 4 first byte = messageId<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * 4 second byte = message length<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * @param buffer
	 * @return
	 */
	public static NetworkMessageHeader readHeader(ByteBuffer buffer) {
		
		NetworkMessageHeader header = null;
		
		if (buffer.capacity() < STATIC_HEADER_LEN)
			return null;
		//4 first byte = messageId
		//|0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|
		int messageId =	buffer.getInt();
		//4 second byte = message length
		//|0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|
		int len =		buffer.getInt();
		
		header = new NetworkMessageHeader(messageId, len);
		return (header);
	}
	
	/**
	 * write NetworkMessageHeader on ByteBuffer<br>
	 * 4 first byte = messageId<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * 4 second byte = message length<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * @param header
	 * @return
	 */
	public static ByteBuffer writeHeader(NetworkMessageHeader header) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		
		//4 first byte = messageId
		//|0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|
		buffer.putInt(header.getId());
		//4 second byte = message length
		//|0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|
		buffer.putInt(header.getLength());
        return (buffer);
	}
}
