package org.fixme.core.protocol;

/**
 * Header protocol message
 * @author jguyet
 * @Commented
 */
public class NetworkProtocolMessage {
	
	/**
	 * SIZE OF HEADER = INT32 + INT32
	 */
	public static final int STATIC_HEADER_LEN = 12;
	
	/**
	 * read message Header<br>
	 * 4 first byte = id<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * 4 next byte = messageId<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * 4 next byte = message length<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * @param buffer
	 * @return
	 */
	public static NetworkMessageHeader readHeader(ByteArrayBuffer buffer) {
		
		NetworkMessageHeader header = null;
		
		if (buffer.size() < STATIC_HEADER_LEN)
			return null;
		//4 first byte = Id
		//|0000 0000 0000 0000 0000 0000 0000 0000|
		int id = buffer.readInt();
		//4 next byte = messageId
		//|0000 0000 0000 0000 0000 0000 0000 0000|
		int messageId =	buffer.readInt();
		//4 next byte = message length
		//|0000 0000 0000 0000 0000 0000 0000 0000|
		int len =		buffer.readInt();
		
		header = new NetworkMessageHeader(id, messageId, len);
		return (header);
	}
	
	/**
	 * write NetworkMessageHeader on ByteBuffer<br>
	 * 4 first byte = id<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * 4 next byte = messageId<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * 4 next byte = message length<br>
	 * |0000 0000 0000 0000 0000 0000 0000 0000|<br>
	 * @param header
	 * @return
	 */
	public static ByteArrayBuffer writeHeader(NetworkMessageHeader header) {
		ByteArrayBuffer buffer = new ByteArrayBuffer();
		
		//4 first byte = Id
		//|0000 0000 0000 0000 0000 0000 0000 0000|
		buffer.writeInt(header.getId());
		//4 next byte = messageId
		//|0000 0000 0000 0000 0000 0000 0000 0000|
		buffer.writeInt(header.getMessageId());
		//4 next byte = message length
		//|0000 0000 0000 0000 0000 0000 0000 0000|
		buffer.writeInt(header.getLength());
        return (buffer);
	}
}
