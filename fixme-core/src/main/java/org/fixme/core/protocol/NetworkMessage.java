package org.fixme.core.protocol;

import java.nio.ByteBuffer;

public abstract class NetworkMessage {
	
	protected ByteBuffer buffer;
	
	public NetworkMessage(ByteBuffer buffer) {
		this.buffer = buffer;
	}
	
	public abstract int messageId();
	
	public abstract ByteBuffer serialize();
	
	public abstract void deserialize();
	
	public ByteBuffer array() {
		
		ByteBuffer content = serialize();
		
		if (content == null)
			content = ByteBuffer.allocate(0);
		
		int length = content.capacity();
		
		NetworkMessageHeader header = new NetworkMessageHeader(this.messageId(), length);
		ByteBuffer serializedHeader = NetworkProtocolMessage.writeHeader(header);
		ByteBuffer serializedMessage = ByteBuffer.allocate(NetworkProtocolMessage.STATIC_HEADER_LEN + length);
		
		//reset position
		serializedHeader.flip();
		content.flip();
		
		serializedMessage.put(serializedHeader);
		serializedMessage.put(content);
		
		serializedMessage.flip();
		return serializedMessage;
	}
}
