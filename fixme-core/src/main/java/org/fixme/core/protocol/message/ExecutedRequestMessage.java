package org.fixme.core.protocol.message;

import java.nio.ByteBuffer;

import org.fixme.core.protocol.NetworkMessage;

public class ExecutedRequestMessage extends NetworkMessage {

	public static final int MESSAGE_ID = 1004;
	
	public ExecutedRequestMessage(ByteBuffer buffer) {
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
	public ByteBuffer serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deserialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
