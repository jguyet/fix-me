package org.fixme.core.protocol.message;

import org.fixme.core.protocol.ByteArrayBuffer;
import org.fixme.core.protocol.NetworkMessage;

public class ExecutedRequestMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1004;
	
	public ExecutedRequestMessage(ByteArrayBuffer buffer) {
		super(buffer);
	}
	
	public ExecutedRequestMessage() {
		
	}
	
	@Override
	public int messageId() {
		return MESSAGE_ID;
	}

	@Override
	public String getName() {
		return "ExecutedRequestMessage";
	}

	@Override
	public void serialize(ByteArrayBuffer buffer) {
		
	}

	@Override
	public void deserialize(ByteArrayBuffer buffer) {
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
