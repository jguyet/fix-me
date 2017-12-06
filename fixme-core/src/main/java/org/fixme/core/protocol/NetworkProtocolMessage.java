package org.fixme.core.protocol;

import java.nio.ByteBuffer;

public class NetworkProtocolMessage {
	
	/**
	 * SIZE OF HEADER = INT32 + INT32
	 */
	public static final int STATIC_HEADER_LEN = 8;
	
	/**
	 * read message Header<br>
	 * 4 first byte = messageId<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * 4 second byte = message length<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * @param buffer
	 * @return
	 */
	public static NetworkMessageHeader readHeader(ByteArrayBuffer buffer) {
		
		NetworkMessageHeader header = null;
		
		if (buffer.getSize() < STATIC_HEADER_LEN)
			return null;
		//4 first byte = messageId
		//|0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|
		int messageId =	buffer.readInt();
		//4 second byte = message length
		//|0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|
		int len =		buffer.readInt();
		
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
	public static ByteArrayBuffer writeHeader(NetworkMessageHeader header) {
		ByteArrayBuffer buffer = new ByteArrayBuffer();
		
		//4 first byte = messageId
		//|0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|
		buffer.putInt(header.getId());
		//4 second byte = message length
		//|0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000|
		buffer.putInt(header.getLength());
        return (buffer);
	}
}
